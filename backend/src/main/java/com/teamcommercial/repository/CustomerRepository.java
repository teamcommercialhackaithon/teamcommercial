package com.teamcommercial.repository;

import com.teamcommercial.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    List<Customer> findByCustomerNameContainingIgnoreCase(String customerName);
    
    Optional<Customer> findByEmailId(String emailId);
    
    List<Customer> findByCity(String city);
    
    List<Customer> findByState(String state);
    
    List<Customer> findByCommunicationPreference(String communicationPreference);
    
    Optional<Customer> findBySerial(String serial);
}

