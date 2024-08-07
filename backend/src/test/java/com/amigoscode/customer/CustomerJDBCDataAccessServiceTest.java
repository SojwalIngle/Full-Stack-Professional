package com.amigoscode.customer;

import com.amigoscode.AbstractTestContainers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getjdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer(
                Faker.name().fullName(),
                Faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);

        //When
        List<Customer> actual = underTest.selectAllCustomers();

        //Then
        Assertions.assertThat(actual).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(x -> x.getEmail().equals(email)).map(c->c.getId()).findFirst().orElseThrow();

        //when
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(id);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //Given
        int id = -1;

        //when
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        Assertions.assertThat(actual).isEmpty();
    }



    @Test
    void insertCustomer() {
        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        //When
        Integer id = underTest.selectAllCustomers().stream().filter(x -> x.getEmail().equals(email)).map(c->c.getId()).findFirst().orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);
        //Then
        Assertions.assertThat(actual).isNotEmpty();
    }

    @Test
    void existPersonWithEmail() {
        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        //when
        boolean actual = underTest.existPersonWithEmail(email);

        //Then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void existPersonWithEmailWhenEmailNotExist() {
        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //when
        boolean actual = underTest.existPersonWithEmail(email);

        //Then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void existPersonWihId() {

        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(x -> x.getEmail().equals(email)).map(c->c.getId()).findFirst().orElseThrow();

        //when
        boolean actual = underTest.existPersonWihId(id);

        //Then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void existPersonWihIdWhenIdNotExist() {

        //Given
        Integer id = -1;

        //when
        boolean actual = underTest.existPersonWihId(id);

        //Then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(x -> x.getEmail().equals(email)).map(c->c.getId()).findFirst().orElseThrow();

        //when
        underTest.deleteCustomerById(id);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isNotPresent();

    }

    @Test
    void updateCustomerName() {
        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(x -> x.getEmail().equals(email)).map(c->c.getId()).findFirst().orElseThrow();

        var newName = "foo";

        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(id);
            Assertions.assertThat(c.getName()).isEqualTo(newName);
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateCustomerEmail() {
        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(x -> x.getEmail().equals(email)).map(c->c.getId()).findFirst().orElseThrow();

        var newEmail = "foo@gmail.com";

        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(id);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(newEmail);
            Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateCustomerAge() {
        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(x -> x.getEmail().equals(email)).map(c->c.getId()).findFirst().orElseThrow();

        var newAge = 10;

        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(id);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(newAge);
        });

    }

    @Test
    void updateCustomerAllDetails() {

        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID() ;

        //Given
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();



        Customer update = new Customer();
        update.setId(id);
        update.setName("sam");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(22);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValue(update);

    }

    @Test
    void updateCustomerNoDataChange() {
        //Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream().filter(x -> x.getEmail().equals(email)).map(c->c.getId()).findFirst().orElseThrow();


        //When nothing to update
        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(id);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }
}