package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceFoundException;
import com.amigoscode.exception.RequestValidationExeception;
import com.amigoscode.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();

        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomerById() {
        int id = 10;
        Customer customer = new Customer(id , "Alex" , "alex@gmail,com" , 19);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

      Customer actual= underTest.getCustomerById(id);
      assertThat(actual).isEqualTo(customer);
    }

    @Test
    void WilThrownGetCustomerByIdReturnEmptyOptional() {
        int id = 10;

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with this id = %s not found".formatted(id));
    }

    @Test
    void addCustomer() {
        //Given
        int id = 10;
        String email = "alex@gmail,com";
        Customer customer = new Customer(id , "Alex" , email , 19);

        when(customerDao.existPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex" , email,12);
        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);


        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void willThrowExcpetionCustomerExist() {
        //Given
        int id = 10;
        String email = "alex@gmail,com";
        Customer customer = new Customer(id , "Alex" , email , 19);

        when(customerDao.existPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex" , email,12);
        assertThatThrownBy(() -> underTest.addCustomer(request)).isInstanceOf(DuplicateResourceFoundException.class)
                .hasMessage("Customer With this email : [%S] is Already exists".formatted(request.email()));

        verify(customerDao,never()).insertCustomer(any());
    }

    @Test
    void deleteByCustomerId() {
        int id = 10;
        when(customerDao.existPersonWihId(id)).thenReturn(true);
        //when
        underTest.deleteByCustomerId(id);
        //then
       verify(customerDao).deleteCustomerById(id);

    }

    @Test
    void WillThrowndeleteByCustomerIdNotFound() {
        int id = 10;
        when(customerDao.existPersonWihId(id)).thenReturn(false);
        //when
        assertThatThrownBy(() -> underTest.deleteByCustomerId(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with this id : [%s] is not found".formatted(id));

        verify(customerDao,never()).deleteCustomerById(id);

    }

    @Test
    void updateCustomerAllProperties() {
        //Given
        int id = 10;
        Customer customer = new Customer(id , "Alex" , "alex@gmail,com" , 19);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEamil = "alexandro@gamil.com";
        CustomerUpdateRequest updateReq = new CustomerUpdateRequest("Alexandro", newEamil, 23);

        when(customerDao.existPersonWithEmail(newEamil)).thenReturn(false);
        //when
        underTest.updateCustomer(id,updateReq);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateReq.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateReq.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateReq.age());
    }

    @Test
    void updateCustomerOnlyName() {
        //Given
        int id = 10;
        Customer customer = new Customer(id , "Alex" , "alex@gmail,com" , 19);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateReq = new CustomerUpdateRequest("Alexandro", null, null);

        //when
        underTest.updateCustomer(id,updateReq);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateReq.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerOnlyEmail() {
        //Given
        int id = 10;
        Customer customer = new Customer(id , "Alex" , "alex@gmail,com" , 19);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEamil = "alexandro@gamil.com";
        CustomerUpdateRequest updateReq = new CustomerUpdateRequest(null, newEamil, null);

        //when
        underTest.updateCustomer(id,updateReq);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEamil);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerOnlyAge() {
        //Given
        int id = 10;
        Customer customer = new Customer(id , "Alex" , "alex@gmail,com" , 19);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateReq = new CustomerUpdateRequest(null, null, 22);

        //when
        underTest.updateCustomer(id,updateReq);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateReq.age());
    }

    @Test
    void updateCustomerThrowExceptionWhenEmailAlreadyTaken() {
        //Given
        int id = 10;
        Customer customer = new Customer(id , "Alex" , "alex@gmail,com" , 19);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEamil = "alexandro@gamil.com";
        CustomerUpdateRequest updateReq = new CustomerUpdateRequest("Alexandro", newEamil, 23);

        when(customerDao.existPersonWithEmail(newEamil)).thenReturn(true);
        //when
       assertThatThrownBy(() ->  underTest.updateCustomer(id,updateReq)).isInstanceOf(DuplicateResourceFoundException.class)
               .hasMessage("email already taken");


        verify(customerDao,never()).updateCustomer(any());
    }

//    @Test
//    void updateCustomerNoChanges() {
//        //Given
//        int id = 10;
//        Customer customer = new Customer(id , "Alex" , "alex@gmail,com" , 19);
//        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
//
//        String newEamil = "alexandro@gamil.com";
//        CustomerUpdateRequest updateReq = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());
//
//        //when
//        assertThatThrownBy(() -> underTest.updateCustomer(id,updateReq)).isInstanceOf(RequestValidationExeception.class)
//                .hasMessage("no data changes found");
//
//        //Then
//
//        verify(customerDao,never()).updateCustomer(any());
//
//    }



}