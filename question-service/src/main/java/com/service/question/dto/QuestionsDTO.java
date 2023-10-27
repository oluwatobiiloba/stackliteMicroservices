package com.service.question.dto;

import com.service.question.model.Answer;
import com.service.question.model.User;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionsDTO {

    private String uuid;

    private Integer id;

    private User user;

    private String question;

    private Integer status;

    private List<Answer> answers;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
