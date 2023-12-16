package com.service.mailer.controller;

import com.service.mailer.dto.SendBulkEmailDto;
import com.service.mailer.dto.SendMailDTO;
import com.service.mailer.handlers.ResponseHandler;
import com.service.mailer.service.MailerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mailer")
public class Mailer {
    private final MailerService mailerService;
    private final ResponseHandler responseHandler;

    public Mailer(MailerService mailerService, ResponseHandler responseHandler) {
        this.mailerService = mailerService;
        this.responseHandler = responseHandler;
    }


    @GetMapping("/all")
    private ResponseEntity<String> getMails(@RequestParam(required = false) Map<String,String> requestParams){
        Map<String,Object> emails = mailerService.getEmails(requestParams);

        if (emails.isEmpty()) {
            return responseHandler.sendResponse(null, HttpStatus.NOT_FOUND, null, null, "Successful");
        }
        return responseHandler.sendResponse(emails, HttpStatus.FOUND, null, null, "Successful");
    }

    @PostMapping("/send/single")
    @CircuitBreaker(name = "email-service", fallbackMethod = "sendSingleFallback")
    private ResponseEntity<String> sendSingle(@RequestBody SendMailDTO sendMailDTO) {
        try {
            mailerService.sendEmail(sendMailDTO);
            return responseHandler.sendResponse(null, HttpStatus.ACCEPTED, null, null, "Successfully queued email");
        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseEntity<String> sendSingleFallback(SendMailDTO sendMailDTO, Throwable t) {
        return responseHandler.sendResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, null, null, "Fallback: Unable to send single email");
    }

    @PostMapping("/send/bulk")
    @CircuitBreaker(name = "email-service",fallbackMethod = "sendBulkFallback")
    private ResponseEntity<String> sendBulk(@RequestBody SendBulkEmailDto sendBulkEmailDto){
        try {
            mailerService.sendBulkEmail(sendBulkEmailDto);
            return responseHandler.sendResponse(null, HttpStatus.ACCEPTED, null, null, "Successfully queued bulk email");
        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseEntity<String> sendBulkFallback(SendBulkEmailDto sendBulkEmailDto, Throwable t) {
        return responseHandler.sendResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, null, null, "Fallback: Unable to send bulk email");
    }


}
