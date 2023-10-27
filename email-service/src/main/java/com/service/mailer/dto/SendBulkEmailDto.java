package com.service.mailer.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendBulkEmailDto {


    @NotBlank(message="Template ID is required")
    private String templateId;

    @NotBlank(message="Subject is required")
    private String subject;

    @NotBlank(message="User List is required")
    private List<UserDTO> users;
    private Map<String, String> constants;

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDTO {
        private String email;
        private String username;
        private String body;
        private List<AttachmentDTO> attachments;

    }

    @Data
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttachmentDTO {
        private String name;
        private String url;

    }
}
