package com.service.mailer.service;

import com.service.mailer.dto.SendBulkEmailDto;
import com.service.mailer.dto.SendMailDTO;
import com.service.mailer.mailer.AzureMailer;
import com.service.mailer.models.Emails;
import com.service.mailer.repository.EmailRepo;
import com.service.mailer.utils.Pagination;
import com.service.mailer.utils.SearchResultBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MailerService {
    private final EmailRepo emailRepo;

    private final AzureMailer azureMailer;

    private final String senderAddress = "Stacklite_Admin@2befcba4-7986-41ed-920a-5185024b5538.azurecomm.net";
    @Autowired
    public MailerService(EmailRepo emailRepo, AzureMailer azureMailer){
        this.azureMailer = azureMailer;
        this.emailRepo = emailRepo;

    }

    public Map<String,Object> getEmails(Map<String, String> queryParameters){
        Pageable pageable = Pagination.createPageable(queryParameters);

        Page<Emails> pagedEmails;

        try{
            if(queryParameters.containsKey("name") || queryParameters.containsKey("keyword") ){
                String nameQuery = queryParameters.getOrDefault("name",null);
                String keywordQuery = queryParameters.getOrDefault("keyword",null);

                pagedEmails = emailRepo.findByParams(nameQuery,keywordQuery,pageable);

            }else{
                pagedEmails = emailRepo.findAll(pageable);
            }
            return SearchResultBuilder.buildResult(null,pagedEmails);
        }catch (Exception ignored){

        }
        return null;
    }

    public void sendEmail(SendMailDTO sendMailDTO){
        try{
            String recipientAddress = sendMailDTO.getEmail();
            Integer templateId = Integer.valueOf(sendMailDTO.getTemplateId());
            String subject = sendMailDTO.getSubject();
//        List<SendMailDTO.AttachmentDTO> attachements = sendMailDTO.getAttachments();
            Map<String,String> constants = sendMailDTO.getConstants();
            String content = prepareContent(templateId,constants);
            if( content != null && !recipientAddress.isEmpty()){
                azureMailer.sendEmail(senderAddress,recipientAddress,subject,content);
            }
        }catch(Exception e){
                System.out.println(e.getMessage());
        }
    }

    public void sendBulkEmail(SendBulkEmailDto sendBulkEmailDto){
        try{
            List<String> recipients = sendBulkEmailDto.getUsers()
                    .stream()
                    .map(SendBulkEmailDto.UserDTO::getEmail).toList();

            String subject = sendBulkEmailDto.getSubject();
            Integer templateId = Integer.valueOf(sendBulkEmailDto.getTemplateId());
            Map<String, String> constants = sendBulkEmailDto.getConstants();
            String content = prepareContent(templateId,constants);
            if( content != null && !recipients.isEmpty()){
                azureMailer.sendBulkEmail(senderAddress,recipients,subject,content);
            }
        }catch (Exception ignore){

        }


    }

    private String replaceConstants(String htmlContent, Map<String,String> constants){
        String content = htmlContent;
        var contentObject = new Object() {
            String htmlContent = content;
        };

        if(!contentObject.htmlContent.isEmpty()  && !constants.isEmpty()){
            constants.forEach((key,value)->{
                contentObject.htmlContent =   contentObject.htmlContent.replace("${" + key + "}", value);
            });
        }

        return  contentObject.htmlContent;
    }

    private String prepareContent(Integer templateId,Map<String,String> constants){
        Optional<Emails> emailTemplate = emailRepo.findById(templateId);
        String htmlContent = emailTemplate.map(Emails::getHtmlContent).orElse(null);
        String content = null;
        if(htmlContent != null ) content = replaceConstants(htmlContent,constants);
        return content;
    }
}
