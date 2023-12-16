package com.service.mailer.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.service.mailer.dto.SendMailDTO;

@Service
public class KafkaListenerService {
    private final MailerService mailerService;

    public KafkaListenerService(MailerService mailerService) {
        this.mailerService = mailerService;
    }

    @KafkaListener(topics = "emailNotifier")
    public void processEmailEvent(SendMailDTO sendMailDTO) {
        try {
            mailerService.sendEmail(sendMailDTO);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
