package com.teamcommercial.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "type")
    private String type;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "serial")
    private String serial;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @NotNull(message = "Date is required")
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "payload", columnDefinition = "BYTEA")
    private byte[] payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (date == null) {
            date = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

