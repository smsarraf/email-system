/*
 * Author : Siddharth Saraf
 * Project : SeekAsia
 */
package com.hackerrank.emailsystem.jobs;

import com.hackerrank.emailsystem.dao.UserRepository;
import com.hackerrank.emailsystem.model.User;
import com.hackerrank.emailsystem.model.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * The Class StatusUpdateJob.
 */
@Component
public class StatusUpdateJob {

    /** The Constant logger. */
    private static final Logger logger = LoggerFactory.getLogger(StatusUpdateJob.class);

    /** The user repository. */
    @Autowired
    UserRepository userRepository;
    
    /** The non responsive mins. */
    @Value("${app.status.non-responsive.mins}")
    private int nonResponsiveMins;
    
    /** The inactive mins. */
    @Value("${app.status.inactive.mins}")
    private int inactiveMins;
    
    /** The active mins. */
    @Value("${app.status.non-responsive_to_active.mins}")
    private int activeMins;
    
    /** The non responsive users batch. */
    @Value("${app.job.non-responsie.users.batch.size}")
    private int nonResponsiveUsersBatch;
    
    /** The in active users batch. */
    @Value("${app.job.inactive.users.batch.size}")
    private int inActiveUsersBatch;
    
    /** The active users batch. */
    @Value("${app.job.active.users.batch.size}")
    private int activeUsersBatch;

        /**
		 * Update user subcription status.
		 */
    @Scheduled(cron = "${app.job.status.updator.cron}")
    public void updateUserSubcriptionStatus() {

        logger.info("Update User Status Scheduler Process Started on "+new Timestamp(System.currentTimeMillis()));

        fetchAndProcessNonResponsiveUser();

        fetchAndProcessInactiveUser();

        fetchAndProcessActiveUser();

        logger.info("Update User Status Scheduler Process Completed on "+new Timestamp(System.currentTimeMillis()));

    }

    /**
	 * Fetch and process non responsive user.
	 */
    private void fetchAndProcessNonResponsiveUser() {

        logger.debug("Fetch and Process All Active Users Function Called and Make as Non Responsive");
        try {
            int totalFetchedRecords = 0;
            int startIndex = 0;
            do {
                List<User> nonResponsiveUsers = userRepository.getAllUsersByStatusAndDate(UserStatus.ACTIVE.toString(), nonResponsiveMins, startIndex, nonResponsiveUsersBatch);
                totalFetchedRecords = nonResponsiveUsers.size();
                logger.info("Processing "+nonResponsiveUsers.size()+" Non Responsive Users in Batch "+(startIndex/nonResponsiveUsersBatch)+1);
                updateDBStatus(nonResponsiveUsers, UserStatus.NON_RESPONSIVE);
                startIndex = startIndex + nonResponsiveUsersBatch;
            } while (totalFetchedRecords == nonResponsiveUsersBatch);
        }catch (Exception e){
            logger.error("Error Occurred while processing Active to Non Responsive Status Users, Error Message"+e.getMessage(),e);
        }

    }

    /**
	 * Fetch and process inactive user.
	 */
    private void fetchAndProcessInactiveUser() {

        logger.debug("Fetch and Process All Non Responsive Users Function Called");
        try{
            int totalFetchedRecords = 0;
            int startIndex = 0;
            do {
                List<User> inActiveUsers = userRepository.getAllUsersByStatusAndDate(UserStatus.NON_RESPONSIVE.toString(), inactiveMins, startIndex, inActiveUsersBatch);
                totalFetchedRecords = inActiveUsers.size();
                logger.info("Processing "+inActiveUsers.size()+" Non Responsive Users in Batch "+(startIndex/inActiveUsersBatch)+1);
                updateDBStatus(inActiveUsers, UserStatus.INACTIVE);
                startIndex = startIndex + inActiveUsersBatch;
            } while (totalFetchedRecords == inActiveUsersBatch);
        }catch (Exception e){
            logger.error("Error Occurred while processing Non Responsive Users to Inactive Status, Error Message"+e.getMessage(),e);
        }

    }

    /**
	 * Fetch and process active user.
	 */
    private void fetchAndProcessActiveUser() {

        logger.debug("Fetch and Process All Non Responsive Users to Active Function Called");
        try{
            int totalFetchedRecords = 0;
            int startIndex = 0;
            do {
                List<User> activeUsers = userRepository.getAllRecentlyLoggedNonResponsiveUsers(UserStatus.NON_RESPONSIVE.toString(), activeMins, startIndex, activeUsersBatch);
                totalFetchedRecords = activeUsers.size();
                logger.info("Processing "+activeUsers.size()+" Non Responsive Users in Batch "+(startIndex/activeUsersBatch)+1);
                updateDBStatus(activeUsers, UserStatus.ACTIVE);
                startIndex = startIndex + activeUsersBatch;
            } while (totalFetchedRecords == activeUsersBatch);
        }catch (Exception e){
            logger.error("Error Occurred while processing Non Responsive Users to Inactive Status, Error Message"+e.getMessage(),e);
        }

    }


    /**
	 * Update DB status.
	 *
	 * @param userList the user list
	 * @param status   the status
	 */
    private void updateDBStatus(List<User> userList, UserStatus status) {
        if (!userList.isEmpty()) {
            userList.forEach(user -> {
                user.setStatus(status);
                user.setStatusUpdatedOn(new Timestamp(System.currentTimeMillis()));
            });
            try {
                userRepository.saveAll(userList);
            }catch (Exception e){
                logger.error("Error Occurred while Updating DB Status, Error Message"+e.getMessage(),e);
            }

        }
    }
}
