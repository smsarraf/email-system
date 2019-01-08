/*
 * Author : Siddharth Saraf
 * Project : SeekAsia
 */
package com.hackerrank.emailsystem.jobs;

import com.hackerrank.emailsystem.dao.EmailHistoryRepository;
import com.hackerrank.emailsystem.dao.UserRepository;
import com.hackerrank.emailsystem.model.EmailDeliveryStatus;
import com.hackerrank.emailsystem.model.EmailHistory;
import com.hackerrank.emailsystem.model.User;
import com.hackerrank.emailsystem.model.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class EmailSenderJob.
 */
@Component
public class EmailSenderJob {

    /** The Constant logger. */
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderJob.class);

    /** The non response batch size. */
    @Value("${app.job.email-sender.non-responsie.users.batch.size}")
    private int nonResponseBatchSize;

    /** The active batch size. */
    @Value("${app.job.email-sender.active.users.batch.size}")
    private int activeBatchSize;

    /** The non responssive email frequency. */
    @Value("${app.job.email-sender.non-responsie.frequency}")
    private int nonResponssiveEmailFrequency;

    /** The active user email frequency. */
    @Value("${app.job.email-sender.active_users.frequency}")
    private int activeUserEmailFrequency;

    /** The email title. */
    @Value("${app.email.title}")
    private String emailTitle;

    /** The email body. */
    @Value("${app.email.body}")
    private String emailBody;



    /** The jms template. */
    @Autowired
    private JmsTemplate jmsTemplate;

    /** The user repository. */
    @Autowired
    UserRepository userRepository;

    /** The email history repository. */
    @Autowired
    EmailHistoryRepository emailHistoryRepository;

    /**
	 * Send email.
	 */

    @Scheduled(cron = "${app.job.email-sender.cron}")
    public void SendEmail() {
        logger.info("-------------------------Send Email Scheduler Process Started on "+new Timestamp(System.currentTimeMillis())+"-------------------------------");
        // GET ALL ACTIVE USERS
        sendEmailToAllActiveUser();

        // GET ALL NOT RESPONSIVE_USERS
        sendEmailToAllNonResponsiveUsers();
        logger.info("-------------------------Send Email Scheduler Process Completed on "+new Timestamp(System.currentTimeMillis())+"-------------------------------");

    }

    /**
	 * Send email to all non responsive users.
	 */
    private void sendEmailToAllNonResponsiveUsers() {
        logger.debug("Send Email to Non Responsive Users Funcation Called");
        try {
            int totalFetchedRecords = 0;
            int startIndex = 0;
            do {
                List<User> nonResponsiveUsers = userRepository.getAllUsersByStatusWithLimit(UserStatus.NON_RESPONSIVE.toString(), nonResponssiveEmailFrequency, startIndex, nonResponseBatchSize);
                totalFetchedRecords = nonResponsiveUsers.size();
                logger.info("Processing "+nonResponsiveUsers.size()+" Non Responsive Users in Batch "+(startIndex/nonResponseBatchSize)+1);
                if(totalFetchedRecords > 0)
                    sendEmail(nonResponsiveUsers);
                startIndex = startIndex + nonResponseBatchSize;
            } while (totalFetchedRecords == nonResponseBatchSize);
        }catch (Exception e){
            logger.error("Error Occurred while processing Non Responsive Users, Error Message"+e.getMessage(),e);
        }

    }

    /**
	 * Send email to all active user.
	 */
    private void sendEmailToAllActiveUser() {

        logger.debug("Send Email to Active Users Funcation Called");
        try {
            int totalFetchedRecords = 0;
            int startIndex = 0;
            do {
                List<User> activeUsers = userRepository.getAllUsersByStatusWithLimit(UserStatus.ACTIVE.toString(), activeUserEmailFrequency, startIndex, activeBatchSize);
                totalFetchedRecords = activeUsers.size();
                logger.info("Processing "+activeUsers.size()+" active Users in Batch "+(startIndex/activeBatchSize)+1);
                if(totalFetchedRecords > 0)
                    sendEmail(activeUsers);
                startIndex = startIndex + activeBatchSize;
            } while (totalFetchedRecords == activeBatchSize);
        }catch (Exception e){
            logger.error("Error Occurred while processing Active Users, Error Message"+e.getMessage(),e);
        }

    }

    /**
	 * Send email.
	 *
	 * @param users the users
	 * @function SendEmail send Email to Users
	 */
    private void sendEmail(List<User> users) {

            users.forEach(user -> {
                try {
                    user.setLastEmailSent(new Timestamp(System.currentTimeMillis()));
                    EmailHistory emailHistory = new EmailHistory(user, emailTitle, emailBody, EmailDeliveryStatus.QUEUED, new Timestamp(System.currentTimeMillis()), "", user.getEmail());
                    emailHistoryRepository.save(emailHistory);
                    queueEmail(emailHistory);
                }catch (Exception e){
                    logger.error("Error Occurred while Saving Email History, Error Message"+e.getMessage(),e);
                }
            });

        try {
            userRepository.saveAll(users);
        }catch (Exception e){
            logger.error("Error Occurred while updating User Last Email Sent, Error Message"+e.getMessage(),e);
        }

    }

    /**
	 * Queue email.
	 *
	 * @param emailHistory the email history
	 */
    private void queueEmail(EmailHistory emailHistory) {
        try {
            jmsTemplate.convertAndSend("email.sender", emailHistory);
        }catch (Exception e){
            logger.error("Error Occurred while Sending Message to Queue, Error Message"+e.getMessage(),e);
        }

    }

}
