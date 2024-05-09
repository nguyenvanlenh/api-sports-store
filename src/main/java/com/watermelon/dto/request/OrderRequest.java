package com.watermelon.dto.request;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
		OrderAddressRequest address,
		String note,
		@Positive(message = "Tax must be a positive number")
		Double tax,
	    @Positive(message = "Discount must be a positive number") 
		Double discount,
	    @Positive(message = "Total price must be a positive number")
		Double totalPrice,
	    @Positive(message = "Delivery fee must be a positive number")
		Double deliveryFee,
		@NotNull(message = "Order status cannot be null")
        @Min(value = 1, message = "Order status must be at least 1")
        Integer orderStatus,

        @NotNull(message = "Delivery status cannot be null")
        @Min(value = 1, message = "Delivery status must be at least 1")
        Integer deliveryStatus,

        @NotNull(message = "Delivery method cannot be null")
        @Min(value = 1, message = "Delivery method must be at least 1")
        Integer deliveryMethod,
		String coupondCode,
		String rejectReason,
		@NotNull(message = "List of order details cannot be null")
		Set< @Valid OrderDetailRequest> listOrderDetails
		) {

}
