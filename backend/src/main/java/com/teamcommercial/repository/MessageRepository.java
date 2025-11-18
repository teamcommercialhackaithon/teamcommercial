package com.teamcommercial.repository;

import com.teamcommercial.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findByMessageType(String messageType);
    
    List<Message> findByMessageTextContainingIgnoreCase(String messageText);
    
    List<Message> findAllByOrderByCreatedAtDesc();
    
    List<Message> findByMessageTypeOrderByCreatedAtDesc(String messageType);

    List<Message> findByControlerDeviceAndMessageType(String controlerDevice, String messageType);
    
    // Find messages by controller device and message type containing specific text
    @Query("SELECT m FROM Message m WHERE m.controlerDevice = :controlerDevice AND LOWER(m.messageType) LIKE LOWER(CONCAT('%', :typeContains, '%'))")
    List<Message> findByControlerDeviceAndMessageTypeContaining(@Param("controlerDevice") String controlerDevice, @Param("typeContains") String typeContains);
    
    // Find first message by controller device and message type containing specific text
    @Query("SELECT m FROM Message m WHERE m.controlerDevice = :controlerDevice AND LOWER(m.messageType) LIKE LOWER(CONCAT('%', :typeContains, '%')) ORDER BY m.createdAt DESC")
    List<Message> findFirstByControlerDeviceAndMessageTypeContaining(@Param("controlerDevice") String controlerDevice, @Param("typeContains") String typeContains, Pageable pageable);
}

