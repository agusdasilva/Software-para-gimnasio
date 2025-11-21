package com.example.gymweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.example.gymweb.repository")
@EntityScan("com.example.gymweb.model")
@SpringBootApplication
public class GymwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymwebApplication.class, args);
	}

}
