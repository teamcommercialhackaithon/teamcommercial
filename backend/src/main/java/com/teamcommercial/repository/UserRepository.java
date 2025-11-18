package com.teamcommercial.repository;

import com.teamcommercial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmailId(String emailId);
    
    Optional<User> findByResetToken(String resetToken);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmailId(String emailId);
    
    List<User> findByActive(Boolean active);
    
    List<User> findByResetTokenExpiryBefore(LocalDateTime dateTime);
}

