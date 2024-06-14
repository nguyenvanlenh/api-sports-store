package com.watermelon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class PracticeSpringFreetimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticeSpringFreetimeApplication.class, args);
	}

}
