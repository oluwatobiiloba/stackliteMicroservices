package com.user.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
public class EmailResponse {

    private String timestamp;
    private String Status;
    private String message;

}
