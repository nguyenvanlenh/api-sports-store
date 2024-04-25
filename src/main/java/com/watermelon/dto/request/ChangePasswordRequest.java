package com.watermelon.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record ChangePasswordRequest(
		@NotBlank(message = "username cannot be blank")
		@Length(min = 5, max = 20, message = "username must be between 5-20 characters")
		String username,
		@NotBlank(message = "password cannot be blank")
		@Length(min = 8, max = 20, message = "password must be between 8-20 characters")
		String password,
		@Email(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "invalid email format")
		@NotBlank(message = "email cannot be blank")
		String email,
		@NotBlank(message = "new password cannot be blank")
		@Length(min = 8, max = 20, message = "new password must be between 8-20 characters")
		String newPassword
		) implements Serializable {

}
