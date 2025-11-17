package com.teamcommercial.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id")
    private Long configId;

    @Column(name = "wait_time")
    private Integer waitTime;

    @NotNull(message = "Enable full outage notification flag is required")
    @Column(name = "enable_full_outage_notification", nullable = false)
    private Boolean enableFullOutageNotification = false;

    @NotNull(message = "Partial outage notification flag is required")
    @Column(name = "partial_outage_notification", nullable = false)
    private Boolean partialOutageNotification = false;

    @NotNull(message = "Start/Stop notification flag is required")
    @Column(name = "start_stop_notification", nullable = false)
    private Boolean startStopNotification = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (enableFullOutageNotification == null) {
            enableFullOutageNotification = false;
        }
        if (partialOutageNotification == null) {
            partialOutageNotification = false;
        }
        if (startStopNotification == null) {
            startStopNotification = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

