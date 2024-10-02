package com.watermelon.dto.request;

import jakarta.validation.constraints.Size;

public record CategoryRequest(
		@Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
		String name, 
		@Size(max = 200, message = "Category description must be less than 200 characters")
		String description, 
		boolean active) {
}
