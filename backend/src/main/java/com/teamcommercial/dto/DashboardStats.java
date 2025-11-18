package com.teamcommercial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Long totalEvents;
    private Long totalNotifications;
    private Map<String, Long> eventsByType;
    private Map<String, Long> eventsByDate;
    private Map<String, Long> notificationsByDate;
}

