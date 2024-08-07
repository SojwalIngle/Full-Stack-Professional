package com.amigoscode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    
    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
     void beforeEach() {
         autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //when
        underTest.selectAllCustomers();

        //then
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        int id = -1;

        underTest.selectCustomerById(id);

        //then
        Mockito.verify(customerRepository).findById(id);

    }

    @Test
    void insertCustomer() {
        Customer customer = new Customer(1 , "Alex" , "alex@gmail.com" , 23);

        underTest.insertCustomer(customer);

        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existPersonWithEmail() {
        String email = "alex@gamil.com";

        underTest.existPersonWithEmail(email);

        Mockito.verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existPersonWihId() {
        int id = -1;

        underTest.existPersonWihId(id);

        Mockito.verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        int id = 1;

        underTest.deleteCustomerById(id);

        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer(1 , "Alex" , "alex@gmail.com" , 23);

        underTest.updateCustomer(customer);

        Mockito.verify(customerRepository).save(customer);

    }
}