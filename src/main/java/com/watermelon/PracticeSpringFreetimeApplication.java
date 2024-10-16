package com.watermelon;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
@EnableFeignClients
public class PracticeSpringFreetimeApplication {
	
	@Value("${cloudinary.cloud-name}")
	private String cloudName;

	@Value("${cloudinary.api-key}")
	private String apiKey;

	@Value("${cloudinary.api-secret}")
	private String apiSecret;

	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
				"cloud_name", cloudName,
				"api_key", apiKey,
				"api_secret", apiSecret));
	}
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		SpringApplication.run(PracticeSpringFreetimeApplication.class, args);
	}

}
