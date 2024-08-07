package com.amigoscode.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{


    private final JdbcTemplate jdbcTemplate;

    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                select id , name ,email , age from customer ;
                """;
        return jdbcTemplate.query(sql,customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(int id) {
        var sql = """
                select id,name,email,age from customer where id=?;
                """;
        return jdbcTemplate.query(sql , customerRowMapper,id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql= """
                       INSERT INTO customer(name,email,age)
                       VALUES (? ,?, ? )
                       """;
      int result = jdbcTemplate.update(sql,customer.getName(),customer.getEmail(),customer.getAge());
        System.out.println("" +result);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        var sql = """
                select count(id) from customer where email= ? ;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,email);
         return count!=null && count > 0;
    }

    @Override
    public boolean existPersonWihId(int id) {
        var sql = """
                select count(id) from customer where id= ? ;
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,id);
        return count!=null && count > 0;
    }

    @Override
    public Optional<Customer> deleteCustomerById(int id) {
        var sql = """
                delete from customer where id = ?
                """;
        int result = jdbcTemplate.update(sql , id);
        return null;
    }

    @Override
    public void updateCustomer(Customer update) {

        if(update.getName() != null){
            String sql="update customer SET name = ? where id=?";
            int result = jdbcTemplate.update(sql,update.getName(),update.getId());
            System.out.println("update customer name result = " + result);
        }

        if(update.getAge() != null){
            String sql="update customer SET age = ? where id=?";
            int result = jdbcTemplate.update(sql,update.getAge(),update.getId());
            System.out.println("update customer age result = " + result);
        }

        if(update.getEmail() != null){
            String sql="update customer SET email = ? where id=?";
            int result = jdbcTemplate.update(sql,update.getEmail(),update.getId());
            System.out.println("update customer email result = " + result);
        }
    }
}
