package com.watermelon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
		@NotBlank(message = "username cannot be blank")
		@Size(min = 8, max = 20, message = "Username must be between 8 and 20 characters")
	    String username,
		@Size(min = 8, max = 50, message="Password must be between 8 and 50 characters")
		@NotBlank(message = "password cannot be blank")
		String password){

}
