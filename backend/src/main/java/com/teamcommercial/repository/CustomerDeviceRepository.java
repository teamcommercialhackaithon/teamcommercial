package com.teamcommercial.repository;

import com.teamcommercial.entity.CustomerDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDeviceRepository extends JpaRepository<CustomerDevice, Long> {
    
    List<CustomerDevice> findByCustomerId(String customerId);
    
    List<CustomerDevice> findBySerial(String serial);
    
    List<CustomerDevice> findByStatus(String status);
    
    List<CustomerDevice> findByControllerDevice(String controllerDevice);
    
    Optional<CustomerDevice> findByCustomerIdAndSerial(String customerId, String serial);
}

