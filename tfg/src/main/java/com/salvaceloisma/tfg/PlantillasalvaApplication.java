package com.salvaceloisma.tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
public class PlantillasalvaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantillasalvaApplication.class, args);
	}

}
