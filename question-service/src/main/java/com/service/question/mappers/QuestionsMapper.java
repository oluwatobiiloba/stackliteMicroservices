package com.service.question.mappers;

import com.service.question.dto.QuestionsDTO;
import com.service.question.model.Answer;
import com.service.question.model.Question;
import com.service.question.model.User;

import java.util.*;

public class QuestionsMapper {
    public static QuestionsDTO mapToQuestionDto(Question question){
        if (question == null) return null;
       try{
           return QuestionsDTO.builder()
                   .id(question.getId())
                   .question(question.getQuestion())
                   .status(question.getStatus())
                   .user(mapUser(question.getUser()))
                   .answers(mapAnswers(question.getAnswers()))
                   .build();
       }catch(Exception e){
           return null;
       }
    }

    private static User mapUser(User user){
        if (user == null) return null;
        return  User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getEmail())
                .profileImage(user.getProfileImage())
                .build();
    }

    private static List<Answer> mapAnswers(List<Answer> answers){
        if (answers == null) return Collections.emptyList();
        return answers
                .stream()
                .map(QuestionsMapper::mapAnswer)
                .toList();
    }

    private static Answer mapAnswer(Answer answer){
        if (answer == null) return null;
        return Answer.builder()
                .id(answer.getId())
                .answer(answer.getAnswer())
                .user(mapUser(answer.getUser()))
                .downVotes(answer.getDownVotes())
                .upVotes(answer.getUpVotes())
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .build();
    }
}
