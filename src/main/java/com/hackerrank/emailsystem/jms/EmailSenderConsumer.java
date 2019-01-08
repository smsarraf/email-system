package com.hackerrank.emailsystem.jms;


import com.hackerrank.emailsystem.dao.EmailHistoryRepository;
import com.hackerrank.emailsystem.model.EmailDeliveryStatus;
import com.hackerrank.emailsystem.model.EmailHistory;
import com.hackerrank.emailsystem.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * The Class EmailSenderConsumer.
 */
@Component
public class EmailSenderConsumer {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /** The email service. */
    @Autowired
    EmailService emailService;

    /** The from email. */
    @Value("${app.email.from}")
    private String fromEmail;

    /** The email history repository. */
    @Autowired
    EmailHistoryRepository emailHistoryRepository;

    
    /**
     * Send email.
     *
     * @param emailHistory the email history
     */
    @JmsListener(destination = "email.sender")
    public void sendEmail(EmailHistory emailHistory) { logger.info("Processing Email Message, "+emailHistory.getEmail()+", "+emailHistory.getTitle()); try{
            if(emailService.sendMail(fromEmail, emailHistory.getEmail(), emailHistory.getTitle(), emailHistory.getBody())){
                emailHistory.setEmailDeliveryStatus(EmailDeliveryStatus.SENT);
                emailHistoryRepository.save(emailHistory);
            }else{
                emailHistory.setEmailDeliveryStatus(EmailDeliveryStatus.FAILED);
                emailHistoryRepository.save(emailHistory);
            }
        }catch (Exception e){
            logger.error("Error While sending Email", e);
        }
    }

}
