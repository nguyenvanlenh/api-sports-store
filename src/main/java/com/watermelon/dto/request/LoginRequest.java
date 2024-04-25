package com.watermelon.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record LoginRequest(
		@NotBlank(message = "username cannot be blank")
		String username,
		@NotBlank(message = "password cannot be blank")
		String password) implements Serializable {

}
