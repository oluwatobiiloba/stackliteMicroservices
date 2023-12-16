package com.service.question.service;

import com.service.question.dto.QuestionCreationDto;
import com.service.question.dto.QuestionsDTO;
import com.service.question.mappers.QuestionsMapper;
import com.service.question.model.Question;
import com.service.question.model.User;
import com.service.question.repositories.*;
import com.service.question.utils.Pagination;
import com.service.question.utils.SearchResultBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepo questionRepo;

    private final UsersRepo usersRepo;

    public QuestionService(QuestionRepo questionRepo, UsersRepo usersRepo) {
        this.questionRepo = questionRepo;
        this.usersRepo = usersRepo;
    }

    public Map<String, Object> getQuestions(Map<String, String> queryParameters) {

        Pageable pageable = Pagination.createPageable(queryParameters);
        Page<Question> fetchedPagedQuestions;

        if (queryParameters.containsKey("question") || queryParameters.containsKey("userId")) {

            String questionParam = queryParameters.getOrDefault("question", null);
            Integer userIdParam = Integer.valueOf(queryParameters.getOrDefault("userId", null));
            fetchedPagedQuestions = questionRepo.findByParams(
                    questionParam,
                    userIdParam,
                    pageable);
        } else
            fetchedPagedQuestions = questionRepo.findAll(pageable);

        List<QuestionsDTO> fetchedQuestions = fetchedPagedQuestions
                .getContent()
                .stream()
                .map(QuestionsMapper::mapToQuestionDto)
                .toList();

        return SearchResultBuilder.buildResult(fetchedQuestions, fetchedPagedQuestions);
    }

    public QuestionsDTO createQuestion(QuestionCreationDto questionCreationDto) {

        try {
            Optional<User> user = fetchUser(questionCreationDto.getUserId());
            Question question = Question
                    .builder()
                    .question(questionCreationDto.getQuestion())
                    .user(user.orElse(null))
                    .build();

            Question createdQuestion = questionRepo.save(question);
            return QuestionsMapper.mapToQuestionDto(createdQuestion);
        } catch (Exception ignored) {

        }
        return null;
    }

    public QuestionsDTO getQuestionById(String id){
        try{
            Optional<Question> question = questionRepo.findById(id);
            if(question.isPresent()){
                return QuestionsMapper.mapToQuestionDto(question.get());
            }
        }catch (Exception ignored){

        }
        return null;
    }

    private Optional<User> fetchUser(Integer userId) {
        return usersRepo.findById(userId);
    }
}
