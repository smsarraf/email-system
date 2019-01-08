package com.hackerrank.emailsystem.dao;

import com.hackerrank.emailsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface RoleRepository.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    /**
     * Find by role.
     *
     * @param role the role
     * @return the role
     */
    Role findByRole(String role);

}
