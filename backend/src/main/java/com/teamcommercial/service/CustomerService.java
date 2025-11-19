package com.teamcommercial.service;

import com.teamcommercial.entity.Customer;
import com.teamcommercial.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerByCustomerId(Long customerId) {
        return customerRepository.findById(customerId);
    }

    public Optional<Customer> getCustomerByEmail(String emailId) {
        return customerRepository.findByEmailId(emailId);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long customerId, Customer customerDetails) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        customer.setCustomerName(customerDetails.getCustomerName());
        customer.setCustomerId(customerDetails.getCustomerId());
        customer.setStreetNumber(customerDetails.getStreetNumber());
        customer.setStreetName(customerDetails.getStreetName());
        customer.setUnitNumber(customerDetails.getUnitNumber());
        customer.setCity(customerDetails.getCity());
        customer.setState(customerDetails.getState());
        customer.setPostCode(customerDetails.getPostCode());
        customer.setEmailId(customerDetails.getEmailId());
        customer.setMobileNumber(customerDetails.getMobileNumber());
        customer.setCommunicationPreference(customerDetails.getCommunicationPreference());
        customer.setSerial(customerDetails.getSerial());
        
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        customerRepository.delete(customer);
    }

    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByCustomerNameContainingIgnoreCase(name);
    }

    public List<Customer> getCustomersByCity(String city) {
        return customerRepository.findByCity(city);
    }

    public List<Customer> getCustomersByState(String state) {
        return customerRepository.findByState(state);
    }

    public List<Customer> getCustomersByCommunicationPreference(String preference) {
        return customerRepository.findByCommunicationPreference(preference);
    }
}

