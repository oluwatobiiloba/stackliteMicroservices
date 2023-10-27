package com.service.answers.answerservice.controller;

import com.service.answers.answerservice.dto.AnswerCreationDto;
import com.service.answers.answerservice.dto.AnswersDTO;
import com.service.answers.answerservice.handlers.ResponseHandler;
import com.service.answers.answerservice.model.Question;
import com.service.answers.answerservice.service.AnswerService;
import com.service.answers.answerservice.utils.ResponseUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public  ResponseEntity<String> createAnswer(@Valid @RequestBody AnswerCreationDto answerCreationDto){
        AnswersDTO createdAnswer = answerService.createAnswer(answerCreationDto);
        if (createdAnswer != null)
            return responseHandler.sendResponse(createdAnswer, HttpStatus.CREATED, null, null, "Successful");
        return responseHandler.sendResponse(null, HttpStatus.UNPROCESSABLE_ENTITY, null, null, "Failed to create Answer");
    }

    @GetMapping("/questions")
    @CircuitBreaker(name = "question",fallbackMethod = "getQuestionFallback")
    @TimeLimiter(name = "question",fallbackMethod = "getQuestionFallback")
    @Retry(name="question")
    public CompletableFuture<ResponseEntity<String>>  getQuestion(){
        Mono<ResponseEntity<String>> responseMono = webClientBuilder.build().get()
                .uri("http://question-service/api/v1/questions/search")
                .retrieve()
                .toEntity(String.class);

        ResponseEntity<String> responseEntity = responseMono.block();

        if (responseEntity != null) return CompletableFuture.supplyAsync(() -> responseHandler.sendResponse(responseEntity.getBody(), HttpStatus.FOUND, null, null, "Successful"));
        return CompletableFuture.supplyAsync(() -> responseHandler.sendResponse(null, HttpStatus.NOT_FOUND, null, null, "Failed to Retrieve Questions"));
    }

    public CompletableFuture<ResponseEntity<String>>  getQuestionFallback(RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() -> responseHandler.sendResponse(null, HttpStatus.NOT_FOUND, null, null, "Failed to Retrieve Questions,try again later"));
    }
}
