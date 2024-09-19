package com.watermelon.dto.request;

import com.watermelon.utils.Constants;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record ProductSizeRequest(
		@Min(value = 1, message = "ID must be greater than or equal to 1")
		int id,
		@Min(value = 1, message = "Quantity must be greater than or equal to 1")
		@Max(value = Constants.MAXIMUM_NUMBER_PRODUCTS,
		message = "Quantity must be less than or equal to "+ Constants.MAXIMUM_NUMBER_PRODUCTS) 
		int quantity) {
}
