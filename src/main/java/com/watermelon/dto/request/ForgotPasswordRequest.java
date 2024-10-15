package com.watermelon.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequest(
		@NotBlank(message = "username cannot be blank")
		@Size(min = 8, max = 20, message = "Username must be between 8 and 20 characters")
	    String username,
	    @Email(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "invalid email format")
		@NotBlank(message = "email cannot be blank")
		String email) {

}
