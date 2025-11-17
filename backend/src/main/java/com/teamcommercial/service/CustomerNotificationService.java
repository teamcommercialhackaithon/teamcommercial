package com.teamcommercial.service;

import com.teamcommercial.entity.CustomerNotification;
import com.teamcommercial.repository.CustomerNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerNotificationService {

    private final CustomerNotificationRepository notificationRepository;

    @Autowired
    public CustomerNotificationService(CustomerNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<CustomerNotification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<CustomerNotification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public List<CustomerNotification> getNotificationsByCustomerId(String customerId) {
        return notificationRepository.findByCustomerId(customerId);
    }

    public List<CustomerNotification> getNotificationsByNotifiedStatus(Boolean notified) {
        return notificationRepository.findByNotified(notified);
    }

    public List<CustomerNotification> getNotificationsByType(String type) {
        return notificationRepository.findByType(type);
    }

    public List<CustomerNotification> getNotificationsByMacAddress(String macAddress) {
        return notificationRepository.findByMacAddress(macAddress);
    }

    public List<CustomerNotification> getNotificationsByIpAddress(String ipAddress) {
        return notificationRepository.findByIpAddress(ipAddress);
    }

    public List<CustomerNotification> getNotificationsBySerial(String serial) {
        return notificationRepository.findBySerial(serial);
    }

    public CustomerNotification createNotification(CustomerNotification notification) {
        return notificationRepository.save(notification);
    }

    public CustomerNotification updateNotification(Long id, CustomerNotification notificationDetails) {
        CustomerNotification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        
        notification.setCustomerId(notificationDetails.getCustomerId());
        notification.setMacAddress(notificationDetails.getMacAddress());
        notification.setIpAddress(notificationDetails.getIpAddress());
        notification.setSerial(notificationDetails.getSerial());
        notification.setStartDate(notificationDetails.getStartDate());
        notification.setEndDate(notificationDetails.getEndDate());
        notification.setType(notificationDetails.getType());
        notification.setNotified(notificationDetails.getNotified());
        
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        CustomerNotification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }

    public CustomerNotification markAsNotified(Long id) {
        CustomerNotification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setNotified(true);
        return notificationRepository.save(notification);
    }

    public CustomerNotification markAsUnnotified(Long id) {
        CustomerNotification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setNotified(false);
        return notificationRepository.save(notification);
    }

    public List<CustomerNotification> getExpiredNotifications() {
        return notificationRepository.findByEndDateBefore(LocalDate.now());
    }

    public List<CustomerNotification> getNotificationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return notificationRepository.findByStartDateBetween(startDate, endDate);
    }
}

