package com.service.question.controller;

import com.service.question.dto.QuestionCreationDto;
import com.service.question.dto.QuestionsDTO;
import com.service.question.handlers.ResponseHandler;
import com.service.question.service.QuestionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/questions")
public class Questions {

    public final QuestionService questionService;

    public final ResponseHandler responseHandler;

    public Questions(QuestionService questionService, ResponseHandler responseHandler) {
        this.questionService = questionService;
        this.responseHandler = responseHandler;
    }

    @GetMapping("/search")
    public ResponseEntity<String> getQuestions(@RequestParam(required = false) Map<String, String> queryParameters) {
        Map<String, Object> questions = questionService.getQuestions(queryParameters);
        if (questions.isEmpty()) {
            return responseHandler.sendResponse(null, HttpStatus.NOT_FOUND, null, null, "Successful");
        }
        return responseHandler.sendResponse(questions, HttpStatus.FOUND, null, null, "Successful");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getQuestions( @PathVariable String id) {
        QuestionsDTO question = questionService.getQuestionById(id);
        if (question == null) {
            return responseHandler.sendResponse(null, HttpStatus.NOT_FOUND, null, null, "Successful");
        }
        return responseHandler.sendResponse(question, HttpStatus.FOUND, null, null, "Successful");
    }

    @PostMapping("/create")
    @CircuitBreaker(name = "question-service",fallbackMethod = "createQuestionFallback")
    public ResponseEntity<String> createQuestion(@Valid @RequestBody QuestionCreationDto questionCreationDto) {
        try {
            QuestionsDTO createdQuestion = questionService.createQuestion(questionCreationDto);

            if (createdQuestion != null)
                return responseHandler.sendResponse(createdQuestion, HttpStatus.CREATED, null, null, "Successful");

            return responseHandler.sendResponse(null, HttpStatus.UNPROCESSABLE_ENTITY, null, null, "Failed to create question");
        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseEntity<String> createQuestionFallback(QuestionCreationDto questionCreationDto, Throwable t) {
        return responseHandler.sendResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, null, null, "Fallback: Unable to create question");
    }




}
