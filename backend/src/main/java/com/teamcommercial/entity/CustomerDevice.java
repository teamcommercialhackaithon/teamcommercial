package com.teamcommercial.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Customer ID is required")
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "serial")
    private String serial;

    @Column(name = "controller_device", length = 1)
    private String controllerDevice;

    @Column(name = "status")
    private String status; // Active or Disconnected

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

