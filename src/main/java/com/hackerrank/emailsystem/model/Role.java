/*
 * Author : Siddharth Saraf
 * Project : SeekAsia
 */
package com.hackerrank.emailsystem.model;

import lombok.Data;

import javax.persistence.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Role.
 */
@Data
@Entity
@Table(name = "role")
public class Role {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -295422703255886288L;
    
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private int id;

    /** The role. */
    private String role;

    /**
     * Instantiates a new role.
     *
     * @param role the role
     */
    public Role(String role) {
        this.role = role;
    }

    /**
     * Instantiates a new role.
     */
    public Role() {
    }
}
