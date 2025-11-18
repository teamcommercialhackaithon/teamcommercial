package com.teamcommercial.controller;

import com.teamcommercial.dto.DashboardStats;
import com.teamcommercial.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Get dashboard statistics for a specific time period
     * @param period - today, week, month, three_months
     * @return DashboardStats
     */
    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getDashboardStats(
            @RequestParam(defaultValue = "today") String period) {
        DashboardStats stats = dashboardService.getDashboardStats(period);
        return ResponseEntity.ok(stats);
    }
}

