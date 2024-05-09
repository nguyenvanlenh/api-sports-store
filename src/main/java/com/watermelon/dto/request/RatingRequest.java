package com.watermelon.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RatingRequest(
		@NotBlank(message = "Content must not be blank")
		String content,
        @Positive(message = "Star must be a positive number") 
		@Min(value = 1, message = "Star must be at least 1")
		@Max(value = 5, message = "Star must be at most 5") 
		int star,
        @NotNull(message = "Product ID cannot be null")
		Long productId,
        @NotNull(message = "User ID cannot be null") 
		Long userId) {

}
