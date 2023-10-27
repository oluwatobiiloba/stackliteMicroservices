package com.user.service.mappers;

import java.util.stream.Collectors;

import com.user.service.dto.*;
import com.user.service.model.User;
import org.springframework.security.core.GrantedAuthority;

public class userMapper {
    public static UserRespDto mapToUserDto(User user) {
        return new UserRespDto(
                user.getId(),
                user.getUuid(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getIsVerified(),
                user.getStack(),
                user.getProfileImage(),
                user.getMeta(),
                user.getAge(),
                user.getNationality(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }
}
