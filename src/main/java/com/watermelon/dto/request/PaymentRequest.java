package com.watermelon.dto.request;

import java.math.BigDecimal;

import com.watermelon.model.enumeration.EPaymentMethod;
import com.watermelon.model.enumeration.EPaymentStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PaymentRequest(
		Long orderId,
		@PositiveOrZero(message = "The total amount must be zero or a positive number")
		BigDecimal amount,
		@PositiveOrZero(message = "Payment fee must be zero or a positive number")
		BigDecimal paymentFee,
		@NotNull(message = "Payment method cannot be null")
		EPaymentMethod paymentMethod,
		@NotNull(message = "Payment status cannot be null")
		EPaymentStatus paymentStatus) {}
