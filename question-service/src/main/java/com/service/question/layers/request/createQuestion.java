package com.service.question.layers.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class createQuestion {

    @NotBlank(message = "Question is required")
    private String question;

    @NotBlank(message = "Password is required")
    private String password;

}
