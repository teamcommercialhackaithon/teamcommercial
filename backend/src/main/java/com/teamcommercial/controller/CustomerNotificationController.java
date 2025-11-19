package com.teamcommercial.controller;

import com.teamcommercial.entity.CustomerNotification;
import com.teamcommercial.service.CustomerNotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerNotificationController {

    private final CustomerNotificationService notificationService;

    @Autowired
    public CustomerNotificationController(CustomerNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<Page<CustomerNotification>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "notificationId") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<CustomerNotification> notifications = notificationService.getAllNotifications(pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerNotification> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerNotification>> getNotificationsByCustomerId(@PathVariable String customerId) {
        List<CustomerNotification> notifications = notificationService.getNotificationsByCustomerId(customerId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/notified/{status}")
    public ResponseEntity<List<CustomerNotification>> getNotificationsByStatus(@PathVariable Boolean status) {
        List<CustomerNotification> notifications = notificationService.getNotificationsByNotifiedStatus(status);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CustomerNotification>> getNotificationsByType(@PathVariable String type) {
        List<CustomerNotification> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/mac/{macAddress}")
    public ResponseEntity<List<CustomerNotification>> getNotificationsByMacAddress(@PathVariable String macAddress) {
        List<CustomerNotification> notifications = notificationService.getNotificationsByMacAddress(macAddress);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/ip/{ipAddress}")
    public ResponseEntity<List<CustomerNotification>> getNotificationsByIpAddress(@PathVariable String ipAddress) {
        List<CustomerNotification> notifications = notificationService.getNotificationsByIpAddress(ipAddress);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/serial/{serial}")
    public ResponseEntity<List<CustomerNotification>> getNotificationsBySerial(@PathVariable String serial) {
        List<CustomerNotification> notifications = notificationService.getNotificationsBySerial(serial);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<CustomerNotification>> getExpiredNotifications() {
        List<CustomerNotification> notifications = notificationService.getExpiredNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CustomerNotification>> getNotificationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CustomerNotification> notifications = notificationService.getNotificationsByDateRange(startDate, endDate);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    public ResponseEntity<CustomerNotification> createNotification(@Valid @RequestBody CustomerNotification notification) {
        CustomerNotification createdNotification = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerNotification> updateNotification(@PathVariable Long id, 
                                                                    @Valid @RequestBody CustomerNotification notification) {
        try {
            CustomerNotification updatedNotification = notificationService.updateNotification(id, notification);
            return ResponseEntity.ok(updatedNotification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/mark-notified")
    public ResponseEntity<CustomerNotification> markAsNotified(@PathVariable Long id) {
        try {
            CustomerNotification notification = notificationService.markAsNotified(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/mark-unnotified")
    public ResponseEntity<CustomerNotification> markAsUnnotified(@PathVariable Long id) {
        try {
            CustomerNotification notification = notificationService.markAsUnnotified(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

