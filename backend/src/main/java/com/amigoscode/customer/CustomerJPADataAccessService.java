package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao{


    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(int id) {
        return customerRepository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
      return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existPersonWihId(int id) {
       return customerRepository.existsCustomerById(id);
    }

    @Override
    public Optional<Customer> deleteCustomerById(int id) {
        customerRepository.deleteById(id);
        return null;
    }

    @Override
    public void updateCustomer(Customer update) {
        customerRepository.save(update);
    }
}
