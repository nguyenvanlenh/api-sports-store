package com.watermelon.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderDetailRequest(
		 	@NotNull(message = "Product ID cannot be null")
		 	@Min(value=1,message = "Product ID must be greater than or equal to 1")
		 	Long idProduct,
	        @NotNull(message = "Quantity cannot be null")
		 	@Positive(message = "Quantity must be greater than 0")
		 	Integer quantity,
	        @NotNull(message = "Price cannot be null") 
		 	@PositiveOrZero(message = "Price must be greater than or equal to 0")
		 	Double price,
	        @NotNull(message = "Discount amount cannot be null")
		 	@PositiveOrZero(message = "Discount amount must be greater than or equal to 0")
		 	Double discountAmount,
	        @NotNull(message = "Size ID cannot be null") 
		 	Integer size,
	        @NotNull(message = "Category ID cannot be null") 
		 	Integer category,
	        @NotNull(message = "Brand ID cannot be null") 
		 	Integer brand,
	        String taxPercent) {

}
