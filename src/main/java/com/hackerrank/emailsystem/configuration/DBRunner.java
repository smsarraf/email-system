package com.hackerrank.emailsystem.configuration;

import com.hackerrank.emailsystem.dao.RoleRepository;
import com.hackerrank.emailsystem.dao.UserRepository;
import com.hackerrank.emailsystem.model.Role;
import com.hackerrank.emailsystem.model.User;
import com.hackerrank.emailsystem.model.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;

// TODO: Auto-generated Javadoc
/**
 * The Class DBRunner.
 */
@Component
public class DBRunner implements CommandLineRunner {

    /** The user repository. */
    @Autowired
    private UserRepository userRepository;

    /** The role repository. */
    @Autowired
    private RoleRepository roleRepository;

    /** The b crypt password encoder. */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /* (non-Javadoc)
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... strings) throws Exception {

        Timestamp t = new Timestamp(System.currentTimeMillis());
        User user = new User();
        user.setId(1L);
        user.setEmail("admin@gmail.com");
        user.setName("ADMIN");
        user.setPassword(bCryptPasswordEncoder.encode("admin"));
        user.setStatus(UserStatus.ACTIVE);
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }
}

