package com.tave.camchelin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CamchelinApplication {
	public static void main(String[] args) {
		SpringApplication.run(CamchelinApplication.class, args);
	}

}
