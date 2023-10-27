package com.service.mailer.controller;

import com.service.mailer.dto.SendBulkEmailDto;
import com.service.mailer.dto.SendMailDTO;
import com.service.mailer.handlers.ResponseHandler;
import com.service.mailer.service.MailerService;
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
    private ResponseEntity<String> sendSingle(@RequestBody SendMailDTO sendMailDTO){
        mailerService.sendEmail(sendMailDTO);
        return responseHandler.sendResponse(null,HttpStatus.ACCEPTED,null,null,"Successfully queued email");
    }

    @PostMapping("/send/bulk")
    private ResponseEntity<String> sendBulk(@RequestBody SendBulkEmailDto sendBulkEmailDto){
        mailerService.sendBulkEmail(sendBulkEmailDto);
        return responseHandler.sendResponse(null,HttpStatus.ACCEPTED,null,null,"Successfully queued bulk email");
    }
}
