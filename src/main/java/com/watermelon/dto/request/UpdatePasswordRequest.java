package com.watermelon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
		@Size(min = 8, max = 50, message="Password must be between 8 and 50 characters")
		@NotBlank(message = "password cannot be blank")
		String oldPassword,
		@Size(min = 8, max = 50, message="New password must be between 8 and 50 characters")
		@NotBlank(message = "New password cannot be blank")
		String newPassword
		) {

}
