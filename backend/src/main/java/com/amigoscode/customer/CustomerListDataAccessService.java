package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        customers.add(new Customer(1 ,"alex" , "alex@gmail.com" ,21));
        customers.add(new Customer(2 ,"jamila" , "jamila@gmail.com" ,21));
    }

    public List<Customer> selectAllCustomers() {
        return customers;
    }

    public Optional<Customer> selectCustomerById(int id) {
        return customers.stream().filter(x -> x.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream().anyMatch(person -> person.getEmail().equals(email));
    }

    @Override
    public boolean existPersonWihId(int id) {
        return customers.stream().anyMatch(x -> x.getId().equals(id));
    }

    @Override
    public Optional<Customer> deleteCustomerById(int id) {
        customers.stream().filter(x -> x.getId().equals(id)).findFirst().ifPresent(customers::remove);
        return null;
    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }
}
