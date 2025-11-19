package com.teamcommercial.service;

import com.teamcommercial.entity.CustomerDevice;
import com.teamcommercial.repository.CustomerDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerDeviceService {

    private final CustomerDeviceRepository customerDeviceRepository;

    @Autowired
    public CustomerDeviceService(CustomerDeviceRepository customerDeviceRepository) {
        this.customerDeviceRepository = customerDeviceRepository;
    }

    public Page<CustomerDevice> getAllCustomerDevices(Pageable pageable) {
        return customerDeviceRepository.findAll(pageable);
    }
    
    public List<CustomerDevice> getAllCustomerDevices() {
        return customerDeviceRepository.findAll();
    }

    public Optional<CustomerDevice> getCustomerDeviceById(Long id) {
        return customerDeviceRepository.findById(id);
    }

    public List<CustomerDevice> getCustomerDevicesByCustomerId(String customerId) {
        return customerDeviceRepository.findByCustomerId(customerId);
    }

    public List<CustomerDevice> getCustomerDevicesBySerial(String serial) {
        return customerDeviceRepository.findBySerial(serial);
    }

    public List<CustomerDevice> getCustomerDevicesByStatus(String status) {
        return customerDeviceRepository.findByStatus(status);
    }

    public CustomerDevice createCustomerDevice(CustomerDevice customerDevice) {
        return customerDeviceRepository.save(customerDevice);
    }

    public CustomerDevice updateCustomerDevice(Long id, CustomerDevice customerDeviceDetails) {
        CustomerDevice customerDevice = customerDeviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer Device not found with id: " + id));
        
        customerDevice.setCustomerId(customerDeviceDetails.getCustomerId());
        customerDevice.setSerial(customerDeviceDetails.getSerial());
        customerDevice.setControllerDevice(customerDeviceDetails.getControllerDevice());
        customerDevice.setStatus(customerDeviceDetails.getStatus());
        
        return customerDeviceRepository.save(customerDevice);
    }

    public void deleteCustomerDevice(Long id) {
        CustomerDevice customerDevice = customerDeviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer Device not found with id: " + id));
        customerDeviceRepository.delete(customerDevice);
    }
}

