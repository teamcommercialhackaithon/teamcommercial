package com.teamcommercial.controller;

import com.teamcommercial.dto.EventDTO;
import com.teamcommercial.entity.Event;
import com.teamcommercial.service.EventService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<Page<EventDTO>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "eventId") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Event> events = eventService.getAllEvents(pageable);
        Page<EventDTO> eventDTOs = events.map(this::convertToDTO);
        return ResponseEntity.ok(eventDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<EventDTO>> getEventsByType(@PathVariable String type) {
        List<Event> events = eventService.getEventsByType(type);
        List<EventDTO> eventDTOs = events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    @GetMapping("/mac/{macAddress}")
    public ResponseEntity<List<EventDTO>> getEventsByMacAddress(@PathVariable String macAddress) {
        List<Event> events = eventService.getEventsByMacAddress(macAddress);
        List<EventDTO> eventDTOs = events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    @GetMapping("/serial/{serial}")
    public ResponseEntity<List<EventDTO>> getEventsBySerial(@PathVariable String serial) {
        List<Event> events = eventService.getEventsBySerial(serial);
        List<EventDTO> eventDTOs = events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<EventDTO>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Event> events = eventService.getEventsByDateRange(startDate, endDate);
        List<EventDTO> eventDTOs = events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<EventDTO>> getRecentEvents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        LocalDateTime sinceDate = (since != null) ? since : LocalDateTime.now().minusDays(1);
        List<Event> events = eventService.getRecentEvents(sinceDate);
        List<EventDTO> eventDTOs = events.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdEvent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, 
                                                 @Valid @RequestBody EventDTO eventDTO) {
        try {
            Event event = convertToEntity(eventDTO);
            Event updatedEvent = eventService.updateEvent(id, event);
            return ResponseEntity.ok(convertToDTO(updatedEvent));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper methods to convert between Entity and DTO
    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setEventId(event.getEventId());
        dto.setType(event.getType());
        dto.setMacAddress(event.getMacAddress());
        dto.setSerial(event.getSerial());
        dto.setMessage(event.getMessage());
        dto.setDate(event.getDate());
        dto.setPayloadFromBytes(event.getPayload());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());
        return dto;
    }

    private Event convertToEntity(EventDTO dto) {
        Event event = new Event();
        event.setEventId(dto.getEventId());
        event.setType(dto.getType());
        event.setMacAddress(dto.getMacAddress());
        event.setSerial(dto.getSerial());
        event.setMessage(dto.getMessage());
        event.setDate(dto.getDate());
        event.setPayload(dto.getPayloadAsBytes());
        return event;
    }
}

