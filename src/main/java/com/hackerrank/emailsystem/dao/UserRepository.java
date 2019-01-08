/*
 * 
 */
package com.hackerrank.emailsystem.dao;

import com.hackerrank.emailsystem.model.User;
import com.hackerrank.emailsystem.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The Interface UserRepository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);

    /**
     * Gets the all users by status and date.
     *
     * @param status the status
     * @param minutes the minutes
     * @param page the page
     * @param size the size
     * @return the all users by status and date
     */
    @Query(value = "SELECT * FROM USER u, USER_ROLES ur WHERE u.STATUS=?1 and DATEDIFF(MINUTE, u.LAST_LOGIN, CURRENT_TIMESTAMP()) > ?2 and u.id=ur.user_id and ur.ROLES_ROLE_ID=2 LIMIT ?3 , ?4", nativeQuery = true)
    List<User> getAllUsersByStatusAndDate(String status, int minutes, int page, int size);

    /**
     * Gets the all recently logged non responsive users.
     *
     * @param status the status
     * @param minutes the minutes
     * @param page the page
     * @param size the size
     * @return the all recently logged non responsive users
     */
    @Query(value = "SELECT * FROM USER u, USER_ROLES ur WHERE u.STATUS=?1 and DATEDIFF(MINUTE, STATUS_UPDATED_ON, u.LAST_LOGIN) < ?2 and u.id=ur.user_id and ur.ROLES_ROLE_ID=2 LIMIT ?3 , ?4", nativeQuery = true)
    List<User> getAllRecentlyLoggedNonResponsiveUsers(String status, int minutes, int page, int size);

    /**
     * Gets the all users by status with limit.
     *
     * @param status the status
     * @param minutes the minutes
     * @param page the page
     * @param size the size
     * @return the all users by status with limit
     */
    @Query(value = "SELECT * FROM USER u, USER_ROLES ur WHERE u.STATUS=?1 and (u.LAST_EMAIL_SENT is null or DATEDIFF(MINUTE, u.LAST_EMAIL_SENT, CURRENT_TIMESTAMP()) >= ?2) and u.id=ur.user_id and ur.ROLES_ROLE_ID=2 LIMIT ?3 , ?4", nativeQuery = true)
    List<User> getAllUsersByStatusWithLimit(String status, int minutes, int page, int size);

}
