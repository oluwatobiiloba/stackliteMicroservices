package com.service.answers.answerservice.service;

import com.service.answers.answerservice.dto.AnswerCreationDto;
import com.service.answers.answerservice.dto.AnswersDTO;
import com.service.answers.answerservice.dto.EmailEvent;
import com.service.answers.answerservice.dto.ServiceMonoDataDto;
import com.service.answers.answerservice.dto.ServiceMonoDto;
import com.service.answers.answerservice.mappers.AnswersMapper;
import com.service.answers.answerservice.mappers.ApiResponseMapper;
import com.service.answers.answerservice.model.*;
import com.service.answers.answerservice.repositories.*;
import com.service.answers.answerservice.utils.Pagination;
import com.service.answers.answerservice.utils.SearchResultBuilder;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AnswerService {
    private final AnswerRepo answerRepo;
    private final UsersRepo usersRepo;
    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;

    private final WebClient.Builder webClientBuilder;

    public AnswerService(AnswerRepo answerRepo, UsersRepo usersRepo, WebClient.Builder webClientBuilder,
            KafkaTemplate<String, EmailEvent> kafkaTemplate) {
        this.answerRepo = answerRepo;
        this.usersRepo = usersRepo;
        this.webClientBuilder = webClientBuilder;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Map<String, Object> getAnswer(Map<String, String> queryParameters) {
        try {
            Pageable pageable = Pagination.createPageable(queryParameters);
            Page<Answer> fetchedPagedAnswers;
            if (queryParameters.containsKey("answer") || queryParameters.containsKey("userId")) {
                String answerQuery = queryParameters.getOrDefault("answer", null);
                Integer userQuery = Integer.valueOf(queryParameters.getOrDefault("userId", null));
                fetchedPagedAnswers = answerRepo.findByParams(
                        answerQuery,
                        userQuery,
                        pageable);
            } else
                fetchedPagedAnswers = answerRepo.findAll(pageable);
            assert fetchedPagedAnswers != null;
            List<AnswersDTO> fetchedAnswer = fetchedPagedAnswers
                    .getContent()
                    .stream()
                    .map(AnswersMapper::mapToAnswerDto)
                    .toList();
            return SearchResultBuilder.buildResult(fetchedAnswer, fetchedPagedAnswers);
        } catch (Exception ignored) {
        }
        return null;
    }

    public AnswersDTO createAnswer(AnswerCreationDto answerCreationDto) {
        try {
            Mono<ResponseEntity<Object>> responseMono = fetchQuestionById(
                    String.valueOf(answerCreationDto.getQuestionId()));

            return responseMono
                    .flatMap(response -> {
                        HttpStatus statusCode = (HttpStatus) response.getStatusCode();

                        if (statusCode == HttpStatus.NOT_FOUND) {
                            return Mono.error(new AnswerService.QuestionNotFoundException("Question not found"));
                        }

                        Object responseBody = response.getBody();
                        ServiceMonoDto body = ApiResponseMapper.mapJsonToDto(responseBody, ServiceMonoDto.class);

                        assert body != null;
                        ServiceMonoDataDto dataDto = body.getData();

                        Integer questionId = dataDto.getId();
                        String question = dataDto.getQuestion();
                        String questionUserEmail = dataDto.getUser().getEmail();

                        User user = getUser(answerCreationDto.getUserId()).orElse(null);

                        Answer createdAnswer = Answer.builder()
                                .answer(answerCreationDto.getAnswer())
                                .questionId(questionId)
                                .user(user)
                                .build();

                        Map<String, String> constants = new HashMap<>();
                        constants.put("question", question);
                        constants.put("answer", answerCreationDto.getAnswer());
                        constants.put("username", dataDto.getUser().getUsername());

                        EmailEvent payloadKafka = EmailEvent.builder()
                                .templateId(3)
                                .email(questionUserEmail)
                                .constants(constants)
                                .subject("New Answer to your post")
                                .build();

                        kafkaTemplate.send("emailNotifier", payloadKafka);

                        return Mono.just(AnswersMapper.mapToAnswerDto(createdAnswer));
                    })
                    .block();
        } catch (QuestionNotFoundException e) {
            throw e;
        }
    }

    @CircuitBreaker(name = "question", fallbackMethod = "getQuestionFallback")
    private Mono<ResponseEntity<Object>> fetchQuestionById(String questionId) {
        return webClientBuilder.build()
                .get()
                .uri(String.format("http://question-service/api/v1/questions/%s", questionId))
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                        System.out.println(e.getStatusCode());
                        return Mono.error(new RuntimeException("Service unavailable", e));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No question found"));
                    }
                });
    }

    private Mono<ResponseEntity<Object>> getQuestionFallback(String questionId, Throwable ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Question Service is not available, please try again in a few minutes"));
    }

    private static class QuestionNotFoundException extends RuntimeException {
        public QuestionNotFoundException(String message) {
            super(message);
        }
    }

    private Optional<User> getUser(Integer userId) {
        return usersRepo.findById(userId);
    }

}
