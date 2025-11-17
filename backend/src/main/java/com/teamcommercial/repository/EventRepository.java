package com.teamcommercial.repository;

import com.teamcommercial.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findByType(String type);
    
    List<Event> findByMacAddress(String macAddress);
    
    List<Event> findBySerial(String serial);
    
    List<Event> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Event> findByDateAfter(LocalDateTime date);
    
    List<Event> findByDateBefore(LocalDateTime date);
    
    List<Event> findByMacAddressAndType(String macAddress, String type);
    
    List<Event> findBySerialAndType(String serial, String type);
    
    List<Event> findByTypeOrderByDateDesc(String type);
    
    List<Event> findAllByOrderByDateDesc();
}

