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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventProcessingService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerNotificationRepository customerNotificationRepository;

    @Autowired
    private CustomerDeviceRepository customerDeviceRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void processEventInTransaction(Event event) {
        processEvent(event);
        // Mark event as processed
        event.setProcessed(true);
        eventRepository.save(event);
    }

    private void processEvent(Event event) {
        if (event.getType() == null || event.getSerial() == null) {
            System.out.println("⚠ Event " + event.getEventId() + " missing type or serial - skipping");
            return;
        }

        // Handle outage events (wan_lost or wan_disconnected)
        if (event.getType().equalsIgnoreCase("wan_lost") || 
            event.getType().equalsIgnoreCase("wan_disconnected")) {
            handleOutageEvent(event);
        }
        
        // Handle resolved events (wan_restored or dhcp_renewed)
        if (event.getType().equalsIgnoreCase("wan_restored") || 
            event.getType().equalsIgnoreCase("dhcp_renewed")) {
            handleResolvedEvent(event);
        }
    }

    private void handleOutageEvent(Event event) {
        // Check if there's an open notification for this serial
        List<CustomerNotification> openNotifications = 
            customerNotificationRepository.findOpenNotificationsBySerial(event.getSerial());
        
        // If notification already exists, skip
        if (!openNotifications.isEmpty()) {
            System.out.println("ℹ Open notification already exists for serial: " + event.getSerial());
            return;
        }
        
        // Find customer_device by serial first to get customer_id
        List<CustomerDevice> devices = customerDeviceRepository.findBySerial(event.getSerial());
        
        if (devices.isEmpty()) {
            System.out.println("⚠ No customer device found with serial: " + event.getSerial());
            return;
        }
        
        CustomerDevice device = devices.get(0);
        
        // Validate customer_id
        if (device.getCustomerId() == null || device.getCustomerId().isEmpty()) {
            System.out.println("⚠ Customer device has null or empty customer_id for serial: " + event.getSerial());
            return;
        }
        
        // Now find customer by customer_id from the device
        Long customerId;
        try {
            customerId = Long.parseLong(device.getCustomerId());
        } catch (NumberFormatException e) {
            System.out.println("⚠ Invalid customer_id format: " + device.getCustomerId() + " for serial: " + event.getSerial());
            return;
        }
        
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        
        if (!customerOpt.isPresent()) {
            System.out.println("⚠ No customer found with customer_id: " + device.getCustomerId());
            return;
        }
        
        Customer customer = customerOpt.get();
        
        // Create new customer notification
        CustomerNotification notification = new CustomerNotification();
        notification.setCustomerId(customer.getCustomerId().toString());
        notification.setSerial(event.getSerial());
        notification.setMacAddress(event.getMacAddress());
        notification.setType(event.getType());
        notification.setStartDate(LocalDate.now());
        notification.setEndDate(null);
        notification.setNotified(false);
        
        customerNotificationRepository.save(notification);
        
        System.out.println("✓ Created customer notification for event " + event.getEventId());
        
        // Send email notification
        sendOutageNotification(device, customer, event, notification);
    }

    private void handleResolvedEvent(Event event) {
        // Check if there's an open notification for this serial
        List<CustomerNotification> openNotifications = 
            customerNotificationRepository.findOpenNotificationsBySerial(event.getSerial());
        
        if (openNotifications.isEmpty()) {
            System.out.println("ℹ No open notification found for serial: " + event.getSerial());
            return;
        }
        
        // Close all open notifications
        for (CustomerNotification notification : openNotifications) {
            notification.setEndDate(LocalDate.now());
            customerNotificationRepository.save(notification);
            System.out.println("✓ Closed notification " + notification.getNotificationId());
        }
        
        // Find customer_device by serial
        List<CustomerDevice> devices = customerDeviceRepository.findBySerial(event.getSerial());
        
        if (devices.isEmpty()) {
            System.out.println("⚠ No customer device found with serial: " + event.getSerial());
            return;
        }
        
        CustomerDevice device = devices.get(0);
        
        // Validate customer_id
        if (device.getCustomerId() == null || device.getCustomerId().isEmpty()) {
            System.out.println("⚠ Customer device has null or empty customer_id for serial: " + event.getSerial());
            return;
        }
        
        // Find customer by customer_id
        Long customerId;
        try {
            customerId = Long.parseLong(device.getCustomerId());
        } catch (NumberFormatException e) {
            System.out.println("⚠ Invalid customer_id format: " + device.getCustomerId() + " for serial: " + event.getSerial());
            return;
        }
        
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        
        if (!customerOpt.isPresent()) {
            System.out.println("⚠ No customer found with customer_id: " + device.getCustomerId());
            return;
        }
        
        Customer customer = customerOpt.get();
        
        // Send resolution email
        sendResolutionNotification(device, customer, event);
    }

    private void sendOutageNotification(CustomerDevice device, Customer customer, Event event, CustomerNotification notification) {
        if (device.getControllerDevice() == null || device.getControllerDevice().isEmpty()) {
            System.out.println("ℹ Device controller_device is empty/null, skipping email");
            return;
        }
        
        // Determine message type based on controller_device value
        String messageType = "Y".equalsIgnoreCase(device.getControllerDevice()) 
            ? "Full Outage" 
            : "Partial Outage";
        
        System.out.println("ℹ Looking for message type: " + messageType);
        
        List<Message> messages = messageRepository.findFirstByControlerDeviceAndMessageTypeContaining(
            device.getControllerDevice(), 
            messageType,
            PageRequest.of(0, 1)
        );
        
        if (messages.isEmpty()) {
            System.out.println("⚠ No message found - Type: " + messageType);
            return;
        }
        
        Message message = messages.get(0);
        System.out.println("✓ Found outage message: " + message.getMessageType());
        
        // Send email notification
        if (customer.getEmailId() != null && !customer.getEmailId().isEmpty()) {
            emailService.sendOutageNotificationEmail(
                customer.getEmailId(),
                customer.getCustomerName(),
                message.getMessageText(),
                event.getSerial(),
                message.getMessageType()
            );
            
            // Update notification as notified
            notification.setNotified(true);
            customerNotificationRepository.save(notification);
            
            System.out.println("✓ Email sent and notification marked as notified");
        }
    }

    private void sendResolutionNotification(CustomerDevice device, Customer customer, Event event) {
        if (device.getControllerDevice() == null || device.getControllerDevice().isEmpty()) {
            System.out.println("ℹ Device controller_device is empty/null, skipping email");
            return;
        }
        
        // Determine message type based on controller_device value
        String messageType = "Y".equalsIgnoreCase(device.getControllerDevice()) 
            ? "Full Outage Resolved" 
            : "Partial Outage Resolved";
        
        System.out.println("ℹ Looking for message type: " + messageType);
        
        List<Message> messages = messageRepository.findFirstByControlerDeviceAndMessageTypeContaining(
            device.getControllerDevice(), 
            messageType,
            PageRequest.of(0, 1)
        );
        
        if (messages.isEmpty()) {
            System.out.println("⚠ No message found - Type: " + messageType);
            return;
        }
        
        Message message = messages.get(0);
        System.out.println("✓ Found resolved message: " + message.getMessageType());
        
        // Send email notification
        if (customer.getEmailId() != null && !customer.getEmailId().isEmpty()) {
            emailService.sendOutageNotificationEmail(
                customer.getEmailId(),
                customer.getCustomerName(),
                message.getMessageText(),
                event.getSerial(),
                message.getMessageType()
            );
            
            System.out.println("✓ Resolution email sent to customer");
        }
    }
}

