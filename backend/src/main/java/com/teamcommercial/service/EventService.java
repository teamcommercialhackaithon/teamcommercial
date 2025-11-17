package com.teamcommercial.service;

import com.teamcommercial.entity.Event;
import com.teamcommercial.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByDateDesc();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getEventsByType(String type) {
        return eventRepository.findByTypeOrderByDateDesc(type);
    }

    public List<Event> getEventsByMacAddress(String macAddress) {
        return eventRepository.findByMacAddress(macAddress);
    }

    public List<Event> getEventsBySerial(String serial) {
        return eventRepository.findBySerial(serial);
    }

    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByDateBetween(startDate, endDate);
    }

    public List<Event> getRecentEvents(LocalDateTime since) {
        return eventRepository.findByDateAfter(since);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        
        event.setType(eventDetails.getType());
        event.setMacAddress(eventDetails.getMacAddress());
        event.setSerial(eventDetails.getSerial());
        event.setMessage(eventDetails.getMessage());
        event.setDate(eventDetails.getDate());
        event.setPayload(eventDetails.getPayload());
        
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        eventRepository.delete(event);
    }

    public List<Event> getEventsByMacAddressAndType(String macAddress, String type) {
        return eventRepository.findByMacAddressAndType(macAddress, type);
    }

    public List<Event> getEventsBySerialAndType(String serial, String type) {
        return eventRepository.findBySerialAndType(serial, type);
    }
}

