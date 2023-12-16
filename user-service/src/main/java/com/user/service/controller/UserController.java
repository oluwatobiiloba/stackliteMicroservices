package com.user.service.controller;

import java.util.Map;

import com.user.service.dto.UserRegistrationDto;
import com.user.service.dto.UserRespDto;
import com.user.service.handlers.ResponseHandler;
import com.user.service.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final ResponseHandler responseHandler;

    private final UserService userService;

    public UserController(ResponseHandler responseHandler, UserService userService) {
        this.responseHandler = responseHandler;
        this.userService = userService;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<String> getUsers(@RequestParam(required = false) Map<String, String> queryParameters) {
        Map<String, Object> users = userService.getAllUsers(queryParameters);
        return responseHandler.sendResponse(users, HttpStatus.FOUND, null, null, "Successful");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "user-service", fallbackMethod = "createUserFallback")
    public ResponseEntity<String> createUser(@RequestBody(required = true) UserRegistrationDto userRegDto) {
        try {
            UserRespDto createdUser = userService.createUser(userRegDto);
            return responseHandler.sendResponse(createdUser, HttpStatus.CREATED, null, null, "Successful");
        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseEntity<String> createUserFallback(UserRegistrationDto userRegDto, Throwable t) {
        return responseHandler.sendResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, null, null, "Fallback: Unable to create user");
    }


}
