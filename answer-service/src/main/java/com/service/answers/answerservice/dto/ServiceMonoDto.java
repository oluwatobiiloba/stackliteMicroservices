package com.service.answers.answerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
public class ServiceMonoDto {

    private ServiceMonoDataDto data;
    private String timestamp;
    private String status;
    private String message;
    private String path;
    private Object extraArgs;
    public ServiceMonoDto() {
        // Default constructor
    }
}
