/*
 * Author : Siddharth Saraf
 * Project : SeekAsia
 */
package com.hackerrank.emailsystem.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The Class EmailHistory.
 */
@Entity
@Data
public class EmailHistory {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -295422703255886286L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /** The user. */
    @ManyToOne
    private User user;
    
    /** The email. */
    private String email;
    
    /** The title. */
    private String title;
    
    /** The body. */
    private String body;
    
    /** The email delivery status. */
    @Enumerated(EnumType.STRING)
    private EmailDeliveryStatus emailDeliveryStatus;
    
    /** The sent date. */
    @CreationTimestamp
    private Timestamp sentDate;
    
    /** The fail reason. */
    private String failReason;

    /**
     * Instantiates a new email history.
     *
     * @param user the user
     * @param title the title
     * @param body the body
     * @param emailDeliveryStatus the email delivery status
     * @param sentDate the sent date
     * @param failReason the fail reason
     * @param email the email
     */
    public EmailHistory(User user, String title, String body, EmailDeliveryStatus emailDeliveryStatus, Timestamp sentDate, String failReason, String email) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.emailDeliveryStatus = emailDeliveryStatus;
        this.sentDate = sentDate;
        this.failReason = failReason;
        this.email = email;
    }

    /**
     * Instantiates a new email history.
     */
    public EmailHistory() {
    }
}
