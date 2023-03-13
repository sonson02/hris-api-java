package com.example.hrisapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class HrisApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrisApiApplication.class, args);
	}

}
