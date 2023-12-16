package com.service.answers.answerservice.controller;

import com.service.answers.answerservice.dto.AnswerCreationDto;
import com.service.answers.answerservice.dto.AnswersDTO;
import com.service.answers.answerservice.dto.ServiceMonoDto;
import com.service.answers.answerservice.handlers.ResponseHandler;
import com.service.answers.answerservice.mappers.ApiResponseMapper;
import com.service.answers.answerservice.service.AnswerService;
import com.service.answers.answerservice.utils.ResponseUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/answers")
public class AnswersController {

    public  final AnswerService answerService;
    public final ResponseUtil responseUtil;

    public final ResponseHandler responseHandler;

    private final WebClient.Builder webClientBuilder;


    public AnswersController(AnswerService answerService, ResponseUtil responseUtil, ResponseHandler responseHandler, WebClient.Builder webClientBuilder) {
        this.answerService = answerService;
        this.responseUtil = responseUtil;
        this.responseHandler = responseHandler;
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/search")
    public ResponseEntity<String> getAnswers(@RequestParam(required = false)Map<String,String> queryParams){
        Map<String,Object> answers = answerService.getAnswer(queryParams);
        if(answers == null) return responseHandler.sendResponse(null, HttpStatus.NOT_FOUND,null,null,"No answer(s) found");
        return responseHandler.sendResponse(answers,HttpStatus.FOUND,null,null,"Answer(s) found");
    }

    @PostMapping("/create")
    @CircuitBreaker(name = "answer-service", fallbackMethod = "createAnswerFallback")
    public ResponseEntity<String> createAnswer(@Valid @RequestBody AnswerCreationDto answerCreationDto) {
        try {
            AnswersDTO createdAnswer = answerService.createAnswer(answerCreationDto);
            HttpStatus status = createdAnswer != null ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
            String message = createdAnswer != null ? "Successful" : "Failed to create Answer";
            return responseHandler.sendResponse(createdAnswer, status, null, null, message);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public ResponseEntity<String> createAnswerFallback(AnswerCreationDto answerCreationDto, Throwable t) {
        return responseHandler.sendResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, null, null, "Fallback: Unable to create answer");
    }




}
