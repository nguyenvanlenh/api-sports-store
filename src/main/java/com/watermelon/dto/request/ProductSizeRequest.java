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
		@Max(value = Constants.QUANTITY_PRODUCT_MAX_BUY,
		message = "Quantity must be less than or equal to "+ Constants.QUANTITY_PRODUCT_MAX_BUY) 
		int quantity) {
}
