package com.service.answers.answerservice.mappers;

import com.service.answers.answerservice.dto.AnswersDTO;
import com.service.answers.answerservice.model.*;


import java.util.*;

public class AnswersMapper {
    public static AnswersDTO mapToAnswerDto(Answer answer){
        if (answer == null) return null;
       try{
           return AnswersDTO.builder()
                   .id(answer.getId())
                   .accepted(answer.getAccepted())
                   .upVotes(answer.getUpVotes())
                   .downVotes(answer.getDownVotes())
                   .answer(answer.getAnswer())
                   .questionId(answer.getQuestionId())
                   .user(mapUser(answer.getUser()))
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


//    private static Question mapQuestion(Question question){
//        if (question == null) return null;
//        return Question.builder()
//                .id(question.getId())
//                .user(mapUser(question.getUser()))
//                .question(question.getQuestion())
//                .build();
//    }
}
