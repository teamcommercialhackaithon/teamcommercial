package com.teamcommercial.service;

import com.teamcommercial.dto.DashboardStats;
import com.teamcommercial.repository.CustomerNotificationRepository;
import com.teamcommercial.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CustomerNotificationRepository notificationRepository;

    /**
     * Get dashboard statistics for a specific time period
     * @param period - today, week, month, three_months
     * @return DashboardStats
     */
    public DashboardStats getDashboardStats(String period) {
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime;
        LocalDate startDate;
        LocalDate endDate = LocalDate.now();

        // Calculate start date based on period
        switch (period.toLowerCase()) {
            case "week":
                startDateTime = endDateTime.minusWeeks(1);
                startDate = endDate.minusWeeks(1);
                break;
            case "month":
                startDateTime = endDateTime.minusMonths(1);
                startDate = endDate.minusMonths(1);
                break;
            case "three_months":
                startDateTime = endDateTime.minusMonths(3);
                startDate = endDate.minusMonths(3);
                break;
            case "today":
            default:
                startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                startDate = LocalDate.now();
                break;
        }

        // Get total counts
        Long totalEvents = eventRepository.countByDateBetween(startDateTime, endDateTime);
        Long totalNotifications = notificationRepository.countByStartDateBetween(startDate, endDate);

        // Get events by type
        Map<String, Long> eventsByType = new HashMap<>();
        List<Object[]> eventTypeResults = eventRepository.countByTypeAndDateBetween(startDateTime, endDateTime);
        for (Object[] result : eventTypeResults) {
            String type = (String) result[0];
            Long count = (Long) result[1];
            eventsByType.put(type != null ? type : "Unknown", count);
        }

        // Get events by date
        Map<String, Long> eventsByDate = new HashMap<>();
        List<Object[]> eventDateResults = eventRepository.countByDateGrouped(startDateTime, endDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Object[] result : eventDateResults) {
            if (result[0] instanceof Date) {
                Date sqlDate = (Date) result[0];
                LocalDate localDate = sqlDate.toLocalDate();
                String dateStr = localDate.format(formatter);
                Long count = (Long) result[1];
                eventsByDate.put(dateStr, count);
            }
        }

        // Get notifications by date
        Map<String, Long> notificationsByDate = new HashMap<>();
        List<Object[]> notificationDateResults = notificationRepository.countByDateGrouped(startDate, endDate);
        for (Object[] result : notificationDateResults) {
            LocalDate date = (LocalDate) result[0];
            String dateStr = date.format(formatter);
            Long count = (Long) result[1];
            notificationsByDate.put(dateStr, count);
        }

        return new DashboardStats(
                totalEvents,
                totalNotifications,
                eventsByType,
                eventsByDate,
                notificationsByDate
        );
    }
}

