package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomers(@PathVariable("id")Integer id){
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public void  registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId")int id){
        customerService.deleteByCustomerId(id);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId")int id,@RequestBody CustomerUpdateRequest request) throws DuplicateResourceFoundException {
        customerService.updateCustomer(id,request);
    }
}
