package com.teamcommercial.controller;

import com.teamcommercial.entity.CustomerDevice;
import com.teamcommercial.service.CustomerDeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-devices")
@CrossOrigin(origins = "*")
public class CustomerDeviceController {

    private final CustomerDeviceService customerDeviceService;

    @Autowired
    public CustomerDeviceController(CustomerDeviceService customerDeviceService) {
        this.customerDeviceService = customerDeviceService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDevice>> getAllCustomerDevices() {
        List<CustomerDevice> customerDevices = customerDeviceService.getAllCustomerDevices();
        return ResponseEntity.ok(customerDevices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDevice> getCustomerDeviceById(@PathVariable Long id) {
        return customerDeviceService.getCustomerDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerDevice>> getCustomerDevicesByCustomerId(@PathVariable String customerId) {
        List<CustomerDevice> customerDevices = customerDeviceService.getCustomerDevicesByCustomerId(customerId);
        return ResponseEntity.ok(customerDevices);
    }

    @GetMapping("/serial/{serial}")
    public ResponseEntity<List<CustomerDevice>> getCustomerDevicesBySerial(@PathVariable String serial) {
        List<CustomerDevice> customerDevices = customerDeviceService.getCustomerDevicesBySerial(serial);
        return ResponseEntity.ok(customerDevices);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CustomerDevice>> getCustomerDevicesByStatus(@PathVariable String status) {
        List<CustomerDevice> customerDevices = customerDeviceService.getCustomerDevicesByStatus(status);
        return ResponseEntity.ok(customerDevices);
    }

    @PostMapping
    public ResponseEntity<CustomerDevice> createCustomerDevice(@Valid @RequestBody CustomerDevice customerDevice) {
        CustomerDevice createdCustomerDevice = customerDeviceService.createCustomerDevice(customerDevice);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerDevice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDevice> updateCustomerDevice(@PathVariable Long id, 
                                                               @Valid @RequestBody CustomerDevice customerDevice) {
        try {
            CustomerDevice updatedCustomerDevice = customerDeviceService.updateCustomerDevice(id, customerDevice);
            return ResponseEntity.ok(updatedCustomerDevice);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerDevice(@PathVariable Long id) {
        try {
            customerDeviceService.deleteCustomerDevice(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

