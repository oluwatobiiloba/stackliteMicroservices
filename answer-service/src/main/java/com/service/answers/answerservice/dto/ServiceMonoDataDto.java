package com.service.answers.answerservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMonoDataDto {
    private String uuid;
    private Integer id;
    private UserDTO user;
    private String question;
    private Integer status;
    private List<AnswersDTO> answers;
    private String createdAt;
    private String updatedAt;

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserDTO {
        private String username;
        private String email;
        private Integer id;
        private String uuid;
    }
}
