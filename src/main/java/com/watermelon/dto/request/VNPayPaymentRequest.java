package com.watermelon.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record VNPayPaymentRequest(
		Long orderId,
		@NotBlank(message = "Bank code cannot be null")
		String bankCode,
		String language,
		@PositiveOrZero(message = "The total amount must be zero or a positive number")
		BigDecimal amount,
		@PositiveOrZero(message = "Payment fee must be zero or a positive number")
		BigDecimal paymentFee
		) {}
