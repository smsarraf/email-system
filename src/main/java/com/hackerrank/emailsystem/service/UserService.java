/*
 * Author : Siddharth Saraf
 * Project : SeekAsia
 */
package com.hackerrank.emailsystem.service;

import com.hackerrank.emailsystem.dao.RoleRepository;
import com.hackerrank.emailsystem.dao.UserRepository;
import com.hackerrank.emailsystem.jobs.StatusUpdateJob;
import com.hackerrank.emailsystem.model.Role;
import com.hackerrank.emailsystem.model.User;
import com.hackerrank.emailsystem.model.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

/**
 * The Class UserService.
 */
@Service
public class UserService {

    /** The Constant logger. */
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /** The user repository. */

    private UserRepository userRepository;

    /** The role repository. */

    private RoleRepository roleRepository;

    /** The b crypt password encoder. */

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    /**
	 * Find user by email.
	 *
	 * @param email the email
	 * @return the user
	 */
    public User findUserByEmail(String email) {
        logger.debug("Find User By Email Address Function called EMAIL["+email+"]");
        try {
            return userRepository.findByEmail(email);
        }catch (Exception e){
            logger.error("Error Occurred While Fetching User by Email Address, Error Message:"+e.getMessage(), e);
        }
        return null;
    }

    /**
	 * Save user.
	 *
	 * @param user the user
	 */
    public User saveUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setStatus(UserStatus.ACTIVE);
            user.setActive(1);
            Role userRole = roleRepository.findByRole("USER");
            user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
            userRepository.save(user);
            return user;
        }catch (Exception e){
            logger.error("Error Occurred While Saving User, Error Message:"+e.getMessage(), e);
        }
        return null;
    }

    /**
	 * Update last login.
	 *
	 * @param email the email
	 * @return the user and update last login
	 */
    public void updateLastLogin(String email) {
        try {
            User user = userRepository.findByEmail(email);
            user.setLastLogin(new Timestamp((System.currentTimeMillis())));
            userRepository.save(user);

        }catch (Exception e){
            logger.error("Error Occurred While fetching and Updating Last Login for User, Error Message:"+e.getMessage(), e);
        }
    }
}
