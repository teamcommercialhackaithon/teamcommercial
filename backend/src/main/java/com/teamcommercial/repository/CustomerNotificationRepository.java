package com.teamcommercial.repository;

import com.teamcommercial.entity.CustomerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerNotificationRepository extends JpaRepository<CustomerNotification, Long> {
    
    List<CustomerNotification> findByCustomerId(String customerId);
    
    List<CustomerNotification> findByNotified(Boolean notified);
    
    List<CustomerNotification> findByType(String type);
    
    List<CustomerNotification> findByMacAddress(String macAddress);
    
    List<CustomerNotification> findByIpAddress(String ipAddress);
    
    List<CustomerNotification> findBySerial(String serial);
    
    List<CustomerNotification> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<CustomerNotification> findByEndDateBefore(LocalDate date);
    
    List<CustomerNotification> findByCustomerIdAndNotified(String customerId, Boolean notified);
}

