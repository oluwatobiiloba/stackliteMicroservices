package com.service.answers.answerservice.layers.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class createAnswer {

    @NotBlank(message = "Answer is required")
    private String answer;

    private String userId;

}
