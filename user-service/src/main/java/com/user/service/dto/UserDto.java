package com.user.service.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;

    private String uuid;

    private String username;

    private String firstName;

    private String lastName;

    private Long phoneNumber;

    private String email;

    private Boolean isVerified;

    private String stack;

    private String profileImage;

    private String meta;

    private String age;

    private String nationality;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}
