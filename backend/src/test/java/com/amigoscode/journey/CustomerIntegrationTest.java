package com.amigoscode.journey;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.amigoscode.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.google.common.collect.RangeMap;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;


   private static final String URI = "/api/v1/customers";

    @Test
    void canRegisterACustomer() {
        //create a registration request
        Faker faker = new Faker();
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        String email = firstname.toLowerCase()+"."+lastname+"@amigoscode.com";
        Random random = new Random();
        int age = random.nextInt(1,100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(firstname+" "+lastname , email , age );

        //send a post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customer
        List<Customer> allCustomer = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer is present
        Customer expectedCustomer = new Customer(firstname+" "+lastname,email,age);

        assertThat(allCustomer).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
        .contains(expectedCustomer);

        var id = allCustomer.stream().filter(x -> x.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);

        //get customer by id
        webTestClient.get()
                .uri(URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        //create a registration request
        Faker faker = new Faker();
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        String email = firstname.toLowerCase()+"."+lastname+"@amigoscode.com";
        Random random = new Random();
        int age = random.nextInt(1,100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(firstname+" "+lastname , email , age );

        //send a post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customer
        List<Customer> allCustomer = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomer.stream().filter(x -> x.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        //delete using test client
        webTestClient.delete()
                .uri(URI+"/{id}" ,id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();


        //get customer by id
        webTestClient.get()
                .uri(URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomerAllProperties() {
        //create a registration request
        Faker faker = new Faker();
        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        String email = firstname.toLowerCase()+"."+lastname+"@amigoscode.com";
        Random random = new Random();
        String name = firstname+" "+lastname;
        int age = random.nextInt(1,100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name , email , age );

        //send a post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customer
        List<Customer> allCustomer = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomer.stream().filter(x -> x.getEmail().equals(email)).map(Customer::getId).findFirst().orElseThrow();

        String updateName = faker.name().fullName() ;
        String updateEmail = faker.internet().safeEmailAddress();
        int updateAge = random.nextInt(1,99);
       CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
              updateName , updateEmail , updateAge
       );

   webTestClient.put()
            .uri(URI+"/{id}",id)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(updateRequest),CustomerUpdateRequest.class)
            .exchange()
            .expectStatus()
            .isOk();

        //get customer by id
        Customer customer =  webTestClient.get()
                .uri(URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>(){})
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(id , updateName , updateEmail, updateAge);

      assertThat(customer).isEqualTo(expected);

}









}
