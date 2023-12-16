package com.user.service.service;

import java.util.*;
import java.util.stream.Collectors;

import com.user.service.dto.UserRegistrationDto;
import com.user.service.dto.UserRespDto;
import com.user.service.mappers.userMapper;
import com.user.service.model.ERole;
import com.user.service.model.Role;
import com.user.service.model.User;
import com.user.service.repositories.RolesRepo;
import com.user.service.repositories.UsersRepo;
import com.user.service.utils.Pagination;
import com.user.service.utils.SearchResultBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class UserService {

    private final UsersRepo usersRepo;

    private final RolesRepo rolesRepo;

    private final WebClient.Builder webClientBuilder;

    public UserService(UsersRepo usersRepo, RolesRepo rolesRepo, WebClient.Builder webClientBuilder) {
        this.usersRepo = usersRepo;
        this.rolesRepo = rolesRepo;
        this.webClientBuilder = webClientBuilder;
    }

    public Map<String, Object> getAllUsers(Map<String, String> queryParameters) {
        Pageable pageable = Pagination.createPageable(queryParameters);

        List<UserRespDto> users = null;
        if (queryParameters.containsKey("username") || queryParameters.containsKey("stack")
                || queryParameters.containsKey("userId")) {

            String usernameParam = queryParameters.get("username");
            String stackParam = queryParameters.get("stack");
            Integer userIdParam = queryParameters.containsKey("userId")
                    ? Integer.parseInt(queryParameters.get("userId"))
                    : null;

            Page<User> userPage = usersRepo.findByParams(
                    usernameParam,
                    null,
                    userIdParam,
                    null,
                    null,
                    stackParam,
                    null,
                    pageable);

            users = userPage.getContent()
                    .stream()
                    .map(userMapper::mapToUserDto)
                    .collect(Collectors.toList());

            return SearchResultBuilder.buildResult(users, userPage);
        } else {
            Page<User> userPage = usersRepo.findAll(pageable);
            users = userPage.getContent()
                    .stream()
                    .map(userMapper::mapToUserDto)
                    .collect(Collectors.toList());
            return SearchResultBuilder.buildResult(users, userPage);
        }
    }

    public UserRespDto createUser(UserRegistrationDto userRegDto) {
        try {
            User user = new User();
            user.setUsername(userRegDto.getUsername());
            user.setFirstName(userRegDto.getFirstName());
            user.setLastName(userRegDto.getLastName());
            user.setPhoneNumber(userRegDto.getPhoneNumber());
            user.setEmail(userRegDto.getEmail());
            user.setPassword(userRegDto.getPassword());

            Set<String> strRoles = userRegDto.getRole();
            List<Role> roles = new ArrayList<>();

            if (strRoles == null) {
                Role userRole = rolesRepo.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                for (String role : strRoles) {
                    switch (role) {
                        case "admin" -> {
                            Role adminRole = rolesRepo.findByName(ERole.ROLE_ADMIN).orElseThrow(
                                    () -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                        }
                        case "mod" -> {
                            Role modRole = rolesRepo.findByName(ERole.ROLE_MANAGER).orElseThrow(
                                    () -> new RuntimeException("Error: Role is not found."));
                            roles.add(modRole);
                        }
                        default -> {
                            Role userRole = rolesRepo.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                        }
                    }
                }
            }
            user.setAuthorities(roles);

            String subject = "Welcome to Stacklite";
            String email = user.getEmail();
            String verification_link = "/api/v1/users/verify-email?token=${verification_token}";

            Map<String,Object> constants = new HashMap<>();
            constants.put("username",user.getUsername());
            constants.put("verification_link",verification_link);


            Map<String,Object> emailReqBody = new HashMap<>();
            emailReqBody.put("templateId", 1);
            emailReqBody.put("constants", constants);
            emailReqBody.put("subject", subject);
            emailReqBody.put("email", email);


            Mono<ResponseEntity<String>> responseMono = webClientBuilder.build().post()
                    .uri("http://email-service/api/mailer/send/single")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(emailReqBody)
                    .retrieve()
                    .toEntity(String.class);

            ResponseEntity<String> responseEntity = responseMono.block();

            assert responseEntity != null;
            if(responseEntity.getStatusCode().is2xxSuccessful()){
                System.out.println(Objects.requireNonNull(responseEntity.getBody()));
            }

            return userMapper.mapToUserDto(usersRepo.save(user));
        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }

}
