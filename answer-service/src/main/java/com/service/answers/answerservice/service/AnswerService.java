package com.service.answers.answerservice.service;

import com.service.answers.answerservice.dto.AnswerCreationDto;
import com.service.answers.answerservice.dto.AnswersDTO;
import com.service.answers.answerservice.mappers.AnswersMapper;
import com.service.answers.answerservice.model.*;
import com.service.answers.answerservice.repositories.*;
import com.service.answers.answerservice.utils.Pagination;
import com.service.answers.answerservice.utils.SearchResultBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AnswerService {
    private final AnswerRepo answerRepo;
    private final QuestionRepo questionRepo;
    private final UsersRepo usersRepo;

    public AnswerService(AnswerRepo answerRepo, QuestionRepo questionRepo, UsersRepo usersRepo) {
        this.answerRepo = answerRepo;
        this.questionRepo = questionRepo;
        this.usersRepo = usersRepo;
    }

    public Map<String,  Object> getAnswer(Map<String,String> queryParameters){
        try{
            Pageable pageable = Pagination.createPageable(queryParameters);
            Page<Answer> fetchedPagedAnswers;
            if (queryParameters.containsKey("answer") || queryParameters.containsKey("userId")){
                String answerQuery = queryParameters.getOrDefault("answer",null);
                Integer userQuery = Integer.valueOf(queryParameters.getOrDefault("userId",null));
                fetchedPagedAnswers = answerRepo.findByParams(
                        answerQuery,
                        userQuery,
                        pageable
                );
            }else fetchedPagedAnswers = answerRepo.findAll(pageable);
            assert fetchedPagedAnswers != null;
            List<AnswersDTO> fetchedAnswer = fetchedPagedAnswers
                                            .getContent()
                                            .stream()
                                            .map(AnswersMapper::mapToAnswerDto)
                                            .toList();
            return SearchResultBuilder.buildResult(fetchedAnswer,fetchedPagedAnswers);
        }catch (Exception ignored) {
        }
        return null;
    }

    public AnswersDTO createAnswer(AnswerCreationDto answerCreationDto){
        try{
            Optional<Question> question = questionRepo.findById(String.valueOf(answerCreationDto.getUserId()));
            User user = getUser(answerCreationDto.getUserId()).isPresent() ? getUser(answerCreationDto.getUserId()).get(): null;
            if(question.isEmpty()) return null;
            Answer createdAnswer = Answer.builder()
                    .answer(answerCreationDto.getAnswer())
                    .question(question.get())
                    .user(user)
                    .build();
            return AnswersMapper.mapToAnswerDto(createdAnswer);
        }catch(Exception ignored){

        }
        return null;
    }

    private Optional<User> getUser(Integer userId){
        return usersRepo.findById(userId);
    }

    public Question fetchQuestions() {
        return (Question) questionRepo.findAll();
    }


}
