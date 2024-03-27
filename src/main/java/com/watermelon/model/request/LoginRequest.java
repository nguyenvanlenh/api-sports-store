package com.watermelon.model.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank(message = "username cannot be blank")
		String username,
		@NotBlank(message = "username cannot be blank")
		String password) {

}
