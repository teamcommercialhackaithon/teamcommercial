package com.teamcommercial.repository;

import com.teamcommercial.entity.CustomerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    // Dashboard queries
    Long countByStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT cn.startDate, COUNT(cn) FROM CustomerNotification cn WHERE cn.startDate BETWEEN :startDate AND :endDate GROUP BY cn.startDate ORDER BY cn.startDate")
    List<Object[]> countByDateGrouped(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find open notifications (where end_date is null) by serial
    @Query("SELECT cn FROM CustomerNotification cn WHERE cn.serial = :serial AND cn.endDate IS NULL")
    List<CustomerNotification> findOpenNotificationsBySerial(@Param("serial") String serial);
}

