package com.service.answers.answerservice.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiResponseMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T mapJsonToDto(Object json, Class<T> dtoClass) {
        try {
            return objectMapper.convertValue(json, dtoClass);
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

}
