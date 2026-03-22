package com.example.EduManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EduManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduManagerApplication.class, args);
	}

}
