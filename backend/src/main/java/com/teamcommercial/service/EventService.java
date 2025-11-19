package com.teamcommercial.service;

import com.teamcommercial.entity.Event;
import com.teamcommercial.entity.Customer;
import com.teamcommercial.entity.CustomerNotification;
import com.teamcommercial.entity.CustomerDevice;
import com.teamcommercial.entity.Message;
import com.teamcommercial.repository.EventRepository;
import com.teamcommercial.repository.CustomerRepository;
import com.teamcommercial.repository.CustomerNotificationRepository;
import com.teamcommercial.repository.CustomerDeviceRepository;
import com.teamcommercial.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final CustomerNotificationRepository customerNotificationRepository;
    private final CustomerDeviceRepository customerDeviceRepository;
    private final MessageRepository messageRepository;
    private final EmailService emailService;

    @Autowired
    public EventService(EventRepository eventRepository, 
                       CustomerRepository customerRepository,
                       CustomerNotificationRepository customerNotificationRepository,
                       CustomerDeviceRepository customerDeviceRepository,
                       MessageRepository messageRepository,
                       EmailService emailService) {
        this.eventRepository = eventRepository;
        this.customerRepository = customerRepository;
        this.customerNotificationRepository = customerNotificationRepository;
        this.customerDeviceRepository = customerDeviceRepository;
        this.messageRepository = messageRepository;
        this.emailService = emailService;
    }

    public Page<Event> getAllEvents(Pageable pageable, Boolean processed) {
        if (processed == null) {
            return eventRepository.findAll(pageable);
        }
        // Use the existing findByProcessed method and convert to Page
        List<Event> events = eventRepository.findByProcessed(processed);
        // Since we have a list, we need to manually paginate
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), events.size());
        List<Event> pageContent = events.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, events.size());
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
        // Save the event first
        Event savedEvent = eventRepository.save(event);
        
        // Check if event type is "wan_lost" or "wan_disconnected"
        if (savedEvent.getType() != null && savedEvent.getSerial() != null &&
            (savedEvent.getType().equalsIgnoreCase("wan_lost") || 
             savedEvent.getType().equalsIgnoreCase("wan_disconnected"))) {
            
            // Check if there's an open notification for this serial
            List<CustomerNotification> openNotifications = 
                customerNotificationRepository.findOpenNotificationsBySerial(savedEvent.getSerial());
            
            // If no open notification exists, create a new one
            if (openNotifications.isEmpty()) {
                // Find customer_device by serial first to get customer_id
                List<CustomerDevice> devices = customerDeviceRepository.findBySerial(savedEvent.getSerial());
                
                if (!devices.isEmpty()) {
                    CustomerDevice device = devices.get(0); // Get first matching device
                    
                    // Now find customer by customer_id from the device
                    Optional<Customer> customerOpt = customerRepository.findById(Long.parseLong(device.getCustomerId()));
                    
                    if (customerOpt.isPresent()) {
                        Customer customer = customerOpt.get();
                        
                        // Create new customer notification
                        CustomerNotification notification = new CustomerNotification();
                        notification.setCustomerId(customer.getCustomerId().toString());
                        notification.setSerial(savedEvent.getSerial());
                        notification.setMacAddress(savedEvent.getMacAddress());
                        notification.setType(savedEvent.getType());
                        notification.setStartDate(LocalDate.now());
                        notification.setEndDate(null); // Open notification
                        notification.setNotified(false);
                        
                        customerNotificationRepository.save(notification);
                        
                        System.out.println("=================================================");
                        System.out.println("✓ Created customer notification for event:");
                        System.out.println("Event ID: " + savedEvent.getEventId());
                        System.out.println("Event Type: " + savedEvent.getType());
                        System.out.println("Serial: " + savedEvent.getSerial());
                        System.out.println("Customer ID: " + customer.getCustomerId());
                        System.out.println("Customer Name: " + customer.getCustomerName());
                        System.out.println("=================================================");
                        
                        // Use the device we already found for email notification
                        CustomerDevice matchingDevice = device;
                        
                        if (matchingDevice != null) {
                            System.out.println("ℹ Found matching device - Serial: " + matchingDevice.getSerial() + 
                                             ", Customer ID: " + matchingDevice.getCustomerId() + 
                                             ", Controller Device: " + matchingDevice.getControllerDevice());
                            
                            // Determine message type based on controller_device value
                            // Y = "Full Outage", N = "Partial Outage"
                            if (matchingDevice.getControllerDevice() != null && !matchingDevice.getControllerDevice().isEmpty()) {
                                String messageType = "Y".equalsIgnoreCase(matchingDevice.getControllerDevice()) 
                                    ? "Full Outage" 
                                    : "Partial Outage";
                                
                                System.out.println("ℹ Looking for message type: " + messageType + 
                                                 " (Controller Device: " + matchingDevice.getControllerDevice() + ")");
                                
                                List<Message> messages = messageRepository.findByControlerDeviceAndMessageType(
                                    matchingDevice.getControllerDevice(), 
                                    messageType
                                );
                                
                                if (!messages.isEmpty()) {
                                    Message message = messages.get(0);
                                    System.out.println("✓ Found outage message: " + message.getMessageType() + 
                                                     " (Controller Device: " + message.getControlerDevice() + ")");
                                    
                                    // Send email notification
                                    if (customer.getEmailId() != null && !customer.getEmailId().isEmpty()) {
                                        emailService.sendOutageNotificationEmail(
                                            customer.getEmailId(),
                                            customer.getCustomerName(),
                                            message.getMessageText(),
                                            savedEvent.getSerial(),
                                            message.getMessageType()
                                        );
                                        
                                        // Update notification as notified
                                        notification.setNotified(true);
                                        customerNotificationRepository.save(notification);
                                        
                                        System.out.println("✓ Notification marked as notified");
                                    } else {
                                        System.out.println("⚠ Customer email not available, skipping email notification");
                                    }
                                } else {
                                    System.out.println("⚠ No message found - Type: " + messageType + 
                                                     ", Controller Device: " + matchingDevice.getControllerDevice());
                                }
                            } else {
                                System.out.println("ℹ Device controller_device is empty/null, skipping email notification");
                            }
                        }
                    } else {
                        System.out.println("=================================================");
                        System.out.println("⚠ No customer found with customer_id: " + device.getCustomerId());
                        System.out.println("Cannot create customer notification for event ID: " + savedEvent.getEventId());
                        System.out.println("=================================================");
                    }
                } else {
                    System.out.println("=================================================");
                    System.out.println("⚠ No customer device found with serial: " + savedEvent.getSerial());
                    System.out.println("Cannot create customer notification for event ID: " + savedEvent.getEventId());
                    System.out.println("=================================================");
                }
            } else {
                System.out.println("=================================================");
                System.out.println("ℹ Open notification already exists for serial: " + savedEvent.getSerial());
                System.out.println("Notification ID: " + openNotifications.get(0).getNotificationId());
                System.out.println("Event ID: " + savedEvent.getEventId() + " was logged without creating new notification");
                System.out.println("=================================================");
            }
        }
        
        // Check if event type is "wan_restored" or "dhcp_renewed"
        if (savedEvent.getType() != null && savedEvent.getSerial() != null &&
            (savedEvent.getType().equalsIgnoreCase("wan_restored") || 
             savedEvent.getType().equalsIgnoreCase("dhcp_renewed"))) {
            
            // Check if there's an open notification for this serial
            List<CustomerNotification> openNotifications = 
                customerNotificationRepository.findOpenNotificationsBySerial(savedEvent.getSerial());
            
            // If open notification exists, close it by setting end date
            if (!openNotifications.isEmpty()) {
                for (CustomerNotification notification : openNotifications) {
                    notification.setEndDate(LocalDate.now());
                    customerNotificationRepository.save(notification);
                    
                    System.out.println("=================================================");
                    System.out.println("✓ Closed customer notification for event:");
                    System.out.println("Event ID: " + savedEvent.getEventId());
                    System.out.println("Event Type: " + savedEvent.getType());
                    System.out.println("Serial: " + savedEvent.getSerial());
                    System.out.println("Notification ID: " + notification.getNotificationId());
                    System.out.println("Customer ID: " + notification.getCustomerId());
                    System.out.println("Start Date: " + notification.getStartDate());
                    System.out.println("End Date: " + notification.getEndDate());
                    System.out.println("=================================================");
                    
                    // Find customer_device by serial first to get customer_id
                    List<CustomerDevice> devices = customerDeviceRepository.findBySerial(savedEvent.getSerial());
                    if (!devices.isEmpty()) {
                        CustomerDevice device = devices.get(0); // Get first matching device
                        
                        // Now find customer by customer_id from the device
                        Optional<Customer> customerOpt = customerRepository.findById(Long.parseLong(device.getCustomerId()));
                        if (customerOpt.isPresent()) {
                            Customer customer = customerOpt.get();
                            
                            // Use the device we already found
                            CustomerDevice matchingDevice = device;
                            
                            if (matchingDevice != null) {
                                System.out.println("ℹ Found matching device - Serial: " + matchingDevice.getSerial() + 
                                                 ", Customer ID: " + matchingDevice.getCustomerId() + 
                                                 ", Controller Device: " + matchingDevice.getControllerDevice());
                                
                                // Determine message type based on controller_device value
                                // Y = "Full Outage Resolved", N = "Partial Outage Resolved"
                                if (matchingDevice.getControllerDevice() != null && !matchingDevice.getControllerDevice().isEmpty()) {
                                    String messageType = "Y".equalsIgnoreCase(matchingDevice.getControllerDevice()) 
                                        ? "Full Outage Resolved" 
                                        : "Partial Outage Resolved";
                                    
                                    System.out.println("ℹ Looking for message type: " + messageType + 
                                                     " (Controller Device: " + matchingDevice.getControllerDevice() + ")");
                                    
                                    List<Message> messages = messageRepository.findFirstByControlerDeviceAndMessageTypeContaining(
                                        matchingDevice.getControllerDevice(), 
                                        messageType,
                                        PageRequest.of(0, 1)
                                    );
                                    
                                    if (!messages.isEmpty()) {
                                        Message message = messages.get(0);
                                        System.out.println("✓ Found resolved message: " + message.getMessageType() + 
                                                         " (Controller Device: " + message.getControlerDevice() + ")");
                                        
                                        // Send email notification
                                        if (customer.getEmailId() != null && !customer.getEmailId().isEmpty()) {
                                            emailService.sendOutageNotificationEmail(
                                                customer.getEmailId(),
                                                customer.getCustomerName(),
                                                message.getMessageText(),
                                                savedEvent.getSerial(),
                                                message.getMessageType()
                                            );
                                            
                                            System.out.println("✓ Resolution email sent to customer");
                                        } else {
                                            System.out.println("⚠ Customer email not available, skipping email notification");
                                        }
                                    } else {
                                        System.out.println("⚠ No message found - Type: " + messageType + 
                                                         ", Controller Device: " + matchingDevice.getControllerDevice());
                                    }
                                } else {
                                    System.out.println("ℹ Device controller_device is empty/null, skipping email notification");
                                }
                            }
                        } else {
                            System.out.println("⚠ No customer found with customer_id: " + device.getCustomerId());
                        }
                    } else {
                        System.out.println("⚠ No customer device found with serial: " + savedEvent.getSerial());
                    }
                }
            } else {
                System.out.println("=================================================");
                System.out.println("ℹ No open notification found for serial: " + savedEvent.getSerial());
                System.out.println("Event ID: " + savedEvent.getEventId() + " (Type: " + savedEvent.getType() + ") was logged without closing notification");
                System.out.println("=================================================");
            }
        }
        
        return savedEvent;
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
        event.setProcessed(eventDetails.getProcessed());
        
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

