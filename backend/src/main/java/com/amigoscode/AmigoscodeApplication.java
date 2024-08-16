package com.amigoscode;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class AmigoscodeApplication {

	public static void main(String[] args) {
		System.out.println("hello");
		SpringApplication.run(AmigoscodeApplication.class, args);

	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository){


		return args -> {
			Faker faker = new Faker();
			var name = faker.name();
			String firstname = name.firstName();
			String lastname = name.lastName();
			int age = new Random().nextInt(16,99);
			String email = firstname.toLowerCase()+"."+lastname.toLowerCase()+"@amigoscode.com";
			Customer customer = (new Customer(
					firstname+" "+ lastname,
					email,
					age
			));
			customerRepository.save(customer);
		};
	}
}
