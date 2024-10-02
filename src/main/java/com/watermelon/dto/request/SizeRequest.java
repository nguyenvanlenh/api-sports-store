package com.watermelon.dto.request;

import jakarta.validation.constraints.Size;

public record SizeRequest(
		@Size(min = 1, max = 10, message = "Size name must be between 1 and 10 characters")
		String name, 
		@Size(max = 200, message = "Size description must be less than 200 characters")
		String description, 
		boolean active) {
}
