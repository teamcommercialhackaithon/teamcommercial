package com.teamcommercial.service;

import com.teamcommercial.entity.User;
import com.teamcommercial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.existsByEmailId(user.getEmailId())) {
            throw new RuntimeException("Email is already registered");
        }

        // Encrypt password if provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        
        // Check username uniqueness if changed
        if (!user.getUsername().equals(userDetails.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new RuntimeException("Username is already taken");
            }
            user.setUsername(userDetails.getUsername());
        }

        // Check email uniqueness if changed
        if (!user.getEmailId().equals(userDetails.getEmailId())) {
            if (userRepository.existsByEmailId(userDetails.getEmailId())) {
                throw new RuntimeException("Email is already registered");
            }
            user.setEmailId(userDetails.getEmailId());
        }

        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setActive(userDetails.getActive());

        // Only update password if a new one is provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public User toggleUserStatus(Long id) {
        User user = getUserById(id);
        user.setActive(!user.getActive());
        return userRepository.save(user);
    }

    public List<User> getActiveUsers() {
        return userRepository.findByActive(true);
    }

    public List<User> getInactiveUsers() {
        return userRepository.findByActive(false);
    }
}

