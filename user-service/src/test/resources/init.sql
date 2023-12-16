

CREATE TABLE `roles` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `role_id` int DEFAULT NULL,
                         `name` varchar(255) NOT NULL,
                         `createdAt` datetime NOT NULL,
                         `updatedAt` datetime NOT NULL,
                         `user_id` int NOT NULL,
                         `userId` int NOT NULL,
                         PRIMARY KEY (`id`)
);


INSERT INTO `roles` (`id`, `role_id`, `name`, `createdAt`, `updatedAt`, `user_id`, `userId`)
VALUES
    (1, 1, 'ROLE_ADMIN', '2023-07-13 04:41:16', '2023-07-13 04:41:16', 705, 705),
    (2, 2, 'ROLE_USER', '2023-07-13 04:43:53', '2023-07-13 04:43:53', 705, 0),
    (3, 3, 'ROLE_MANAGER', '2023-07-13 04:44:14', '2023-07-13 04:44:14', 705, 0);

CREATE TABLE `users` (
                         `uuid` char(36) CHARACTER SET utf8mb4 DEFAULT NULL,
                         `id` int NOT NULL AUTO_INCREMENT,
                         `username` varchar(255) NOT NULL,
                         `password` varchar(255) NOT NULL,
                         `first_name` varchar(255) NOT NULL,
                         `last_name` varchar(255) NOT NULL,
                         `phonenumber` bigint NOT NULL,
                         `email` varchar(255) NOT NULL,
                         `role` int NOT NULL DEFAULT '1',
                         `stack` varchar(255) DEFAULT NULL,
                         `age` varchar(255) DEFAULT NULL,
                         `nationality` varchar(255) DEFAULT NULL,
                         `createdAt` datetime NOT NULL,
                         `updatedAt` datetime NOT NULL,
                         `userId` int DEFAULT NULL,
                         `is_verified` tinyint(1) NOT NULL DEFAULT '0',
                         `passwordResetToken` varchar(255) DEFAULT NULL,
                         `passwordResetTokenExpiresAt` datetime DEFAULT NULL,
                         `profile_image` varchar(2000) CHARACTER SET utf8mb4  DEFAULT NULL,
                         `meta` varchar(2000) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `username` (`username`),
                         UNIQUE KEY `unique_email` (`email`),
                         UNIQUE KEY `unique_phonenumber` (`phonenumber`),
                         KEY `userId` (`userId`),
                         KEY `role` (`role`),
                         CONSTRAINT `users_ibfk_2` FOREIGN KEY (`role`) REFERENCES `roles` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `user_roles` (
                              `id` int unsigned NOT NULL AUTO_INCREMENT,
                              `user_id` int NOT NULL,
                              `role_id` int NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `user_id` (`user_id`),
                              KEY `role_id` (`role_id`),
                              CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                              CONSTRAINT `user_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9;
