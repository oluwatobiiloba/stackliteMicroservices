package com.service.mailer.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMailDTO {

    @NotBlank(message="Template ID is required")
    private String templateId;
    private Map<String, String> constants;

    @NotBlank(message="Recipient Email is required")
    private String email;

    private String replyTo;

    @NotBlank(message="Subject is required")
    private String subject;

    private List<AttachmentDTO> attachments;

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttachmentDTO {
        private String name;
        private String url;
    }

}
