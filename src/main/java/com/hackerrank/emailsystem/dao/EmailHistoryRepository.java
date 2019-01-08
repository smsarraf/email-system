/*
 * 
 */
package com.hackerrank.emailsystem.dao;

import com.hackerrank.emailsystem.model.EmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * The Interface EmailHistoryRepository.
 */
@Repository
public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long> {
    
    /**
     * Find by user id.
     *
     * @param accountId the account id
     * @return the list
     */
    List<EmailHistory> findByUserId(Long accountId);

    /**
     * Gets the all email history by date.
     *
     * @return the all email history by date
     */
    @Query(value = "SELECT count(*) as count, CAST(SENT_DATE AS DATE) as date FROM EMAIL_HISTORY GROUP BY CAST(SENT_DATE AS DATE)", nativeQuery = true)
    List<Map<?,?>> getAllEmailHistoryByDate();

    /**
     * Gets the all email history by date.
     *
     * @param date the date
     * @return the all email history by date
     */
    @Query(value = "SELECT *, CAST(SENT_DATE AS DATE) as date FROM EMAIL_HISTORY  WHERE CAST(SENT_DATE AS DATE) = ?1", nativeQuery = true)
    List<EmailHistory> getAllEmailHistoryByDate(String date);
}
