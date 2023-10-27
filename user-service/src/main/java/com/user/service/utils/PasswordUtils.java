package com.user.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {

    @Value("${SALT_ROUNDS}")
    private static String saltRounds;

    @Value("${SECURE_RANDOM}")
    private String secureRandom;

    private static String generateSalt() {
        return BCrypt.gensalt(10);
    }

    public static String hashPassword(String password) {
        String salt = generateSalt();
        return BCrypt.hashpw(password, salt);
    }
}
