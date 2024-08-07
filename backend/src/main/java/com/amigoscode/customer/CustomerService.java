package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceFoundException;
import com.amigoscode.exception.RequestValidationExeception;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(int id){
        return customerDao.selectCustomerById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with this id = %s not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest request)  {
        //check if email exist
        //add
        if (customerDao.existPersonWithEmail(request.email())){
            throw new DuplicateResourceFoundException("Customer With this email : [%S] is Already exists".formatted(request.email()));
        }
        customerDao.insertCustomer(new Customer(
                request.name(),
                request.email(),
                request.age()
        ));
    }

    public void deleteByCustomerId(int id) {
        //check if customer with this id is available
        if(!customerDao.existPersonWihId(id)){
            throw new ResourceNotFoundException("Customer with this id : [%s] is not found".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Integer customerId , CustomerUpdateRequest updateRequest)  {
        Customer customer = getCustomerById(customerId);

        boolean changes = false;

        if(updateRequest.name() != null && !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());
            changes =true;
        }

        if(updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())){
            customer.setAge(updateRequest.age());
            changes =true;
        }

        if(updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())){
            if(customerDao.existPersonWithEmail(updateRequest.email())){
                throw new DuplicateResourceFoundException("email already taken");
            }

            customer.setEmail(updateRequest.email());
            changes =true;
        }

        if(!changes){
            try {
                throw new RequestValidationExeception("no data changes found");
            } catch (RequestValidationExeception e) {
                throw new RuntimeException(e);
            }
        }

        customerDao.updateCustomer(customer);

    }
}
