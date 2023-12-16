package com.service.answers.answerservice.dto;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailEvent {
    @NotBlank(message = "Template ID is required")
    private Integer templateId;
    private Map<String, String> constants;

    @NotBlank(message = "Recipient Email is required")
    private String email;

    private String replyTo;

    @NotBlank(message = "Subject is required")
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
