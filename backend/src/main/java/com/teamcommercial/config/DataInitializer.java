package com.teamcommercial.config;

import com.teamcommercial.entity.User;
import com.teamcommercial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create test user if no users exist
        if (userRepository.count() == 0) {
            User testUser = new User();
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setUsername("testuser");
            testUser.setPassword(passwordEncoder.encode("test123"));
            testUser.setEmailId("testuser@teamcommercial.com");
            testUser.setPhoneNumber("555-0100");
            testUser.setActive(true);
            
            userRepository.save(testUser);
            
            System.out.println("=================================================");
            System.out.println("TEST USER CREATED:");
            System.out.println("Username: testuser");
            System.out.println("Password: test123");
            System.out.println("Email: testuser@teamcommercial.com");
            System.out.println("=================================================");
        }
    }
}

