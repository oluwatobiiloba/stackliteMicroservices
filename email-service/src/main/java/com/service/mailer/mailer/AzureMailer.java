package com.service.mailer.mailer;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

import java.util.List;

@Component
public class AzureMailer  {

    Dotenv dotenv = Dotenv.load();
    private final String comServiceConnectionString = dotenv.get("COMMUNICATION_SERVICES_CONNECTION_STRING");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String getConnectionString() {
        return comServiceConnectionString;
    }

    public EmailClient createClient() {
        AzureMailer az = new AzureMailer();
        String connectionStr = az.getConnectionString();
        return new EmailClientBuilder()
                .connectionString(connectionStr)
                .buildClient();
    }

    private EmailClient emailClient() {
        return  createClient();
    };





    @Async
    public void sendEmail(String senderAddress, String recipientAddress, String subject, String body) {
        EmailMessage message = new EmailMessage();
        message.setSenderAddress(senderAddress)
                .setToRecipients(recipientAddress)
                .setSubject(subject).setBodyHtml(body);

        SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient().beginSend(message);
        PollResponse<EmailSendResult> response = poller.waitForCompletion();

        logger.info("Operation Id: " + response.getValue().getId());
    }

    @Async
    public void sendBulkEmail(String senderAddress, List<String> recipientAddresses, String subject, String body ){;
        String[] recipientArray = recipientAddresses.toArray(new String[0]);

        EmailMessage message = new EmailMessage()
                .setSenderAddress(senderAddress)
                .setSubject(subject)
                .setBodyHtml(body)
                .setToRecipients(recipientArray);

        SyncPoller<EmailSendResult,EmailSendResult> poller = emailClient()
                .beginSend(message);

        PollResponse<EmailSendResult> response = poller.waitForCompletion();

        System.out.println("Operation Id: " + response.getValue().getId());


    }

}
