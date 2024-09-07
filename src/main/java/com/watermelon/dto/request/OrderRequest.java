package com.watermelon.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import com.watermelon.model.enumeration.EDeliveryMethod;
import com.watermelon.model.enumeration.EDeliveryStatus;
import com.watermelon.model.enumeration.EOrderStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderRequest(
		OrderAddressRequest address,
		
		@NotBlank(message = "Name Customer must not be blank")
		String nameCustomer,
		
		@NotBlank(message = "Email Customer must not be blank")
		@Email(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "invalid email format")
		String emailCustomer,
		
		@NotBlank(message = "PhoneNumber Customer must not be blank")
		@Pattern(regexp = "^0[0-9]{9,10}$", message = "Invalid phone number format. Must start with 0 and be 10 or 11 digits long")
		String phoneNumberCustomer,
		
	    @PositiveOrZero(message = "Total price must be zero or a positive number")
		BigDecimal totalPrice,
		
	    @PositiveOrZero(message = "Delivery fee must be zero or a positive number")
		BigDecimal deliveryFee,
		
		@NotNull(message = "Order status cannot be null")
        EOrderStatus orderStatus,

        @NotNull(message = "Delivery status cannot be null")
        EDeliveryStatus deliveryStatus,

        @NotNull(message = "Delivery method cannot be null")
        EDeliveryMethod deliveryMethod,
		String coupondCode,
		String rejectReason,
		@NotNull(message = "List of order details cannot be null")
		Set< @Valid OrderDetailRequest> listOrderDetails
		) {

}
