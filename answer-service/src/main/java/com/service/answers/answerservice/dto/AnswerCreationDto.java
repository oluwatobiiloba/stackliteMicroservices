package com.service.answers.answerservice.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import jakarta.validation.constraints.NotNull;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerCreationDto {
    @NotNull(message = "Answer is required")
    @NotEmpty
    private String answer;

    @NotNull(message = "Question is required")
    private Integer questionId;

    private  Integer userId;
}
