/*
 * Author : Siddharth Saraf
 * Project : SeekAsia
 */
package com.hackerrank.emailsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.Set;

/**
 * The Class User.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -295422703255886287L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /** The name. */
    @NotEmpty(message = "*Please provide your name")
    private String name;
    
    /** The email. */
    @Column(unique = true)
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    
    /** The password. */
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    private String password;

    /** The last login. */
    private Timestamp lastLogin;

    /** The status. */
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /** The active. */
    private int active;
    
    /** The roles. */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> roles;
    
    /** The created on. */
    @CreationTimestamp
    private Timestamp createdOn;

    /** The status updated on. */
    private Timestamp statusUpdatedOn;

    /** The last email sent. */
    private Timestamp lastEmailSent;

}
