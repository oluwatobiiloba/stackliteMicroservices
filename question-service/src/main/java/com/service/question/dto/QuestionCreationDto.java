package com.service.question.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class QuestionCreationDto {
    @NotNull(message = "question required")
    String question;

    @NotNull(message = "userId required")
    Integer userId;
}
