package com.service.answers.answerservice.dto;

import com.service.answers.answerservice.model.Comment;
import com.service.answers.answerservice.model.User;
import com.service.answers.answerservice.model.Voter;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AnswersDTO {


    private String uuid;

    private Integer id;

    private String answer;

    private Integer downVotes;

    private Boolean accepted;

    private Integer upVotes;

    private User user;

    private Integer questionId;

    private List<Comment> comments;

    private List<Voter> voters;

    private Timestamp updatedAt;

    private Timestamp createdAt;
}
