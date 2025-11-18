package com.teamcommercial.scheduler;

import com.teamcommercial.entity.Event;
import com.teamcommercial.repository.EventRepository;
import com.teamcommercial.service.EventProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventProcessingScheduler {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventProcessingService eventProcessingService;

    // Run every 60 seconds (60000 milliseconds)
    @Scheduled(fixedRate = 60000)
    public void processUnprocessedEvents() {
        try {
            System.out.println("=================================================");
            System.out.println("🔄 Starting event processing job...");
            
            // Find all unprocessed events
            List<Event> unprocessedEvents = eventRepository.findByProcessedFalse();
            
            if (unprocessedEvents.isEmpty()) {
                System.out.println("ℹ No unprocessed events found");
                System.out.println("=================================================");
                return;
            }
            
            System.out.println("📋 Found " + unprocessedEvents.size() + " unprocessed events");
            
            int processedCount = 0;
            int errorCount = 0;
            
            for (Event event : unprocessedEvents) {
                try {
                    // Process each event in its own transaction
                    eventProcessingService.processEventInTransaction(event);
                    processedCount++;
                    System.out.println("✓ Event " + event.getEventId() + " marked as processed");
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("✗ Error processing event " + event.getEventId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("=================================================");
            System.out.println("✓ Processing complete - Processed: " + processedCount + ", Errors: " + errorCount);
            System.out.println("=================================================");
        } catch (Exception e) {
            System.err.println("=================================================");
            System.err.println("✗ Fatal error in event processing scheduler");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=================================================");
        }
    }
}
