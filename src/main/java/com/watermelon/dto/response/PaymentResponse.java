package com.watermelon.dto.response;

import java.math.BigDecimal;

import com.watermelon.model.enumeration.EPaymentMethod;
import com.watermelon.model.enumeration.EPaymentStatus;

import lombok.Builder;
@Builder
public record PaymentResponse(
		Long paymentId,
		BigDecimal amount,
		BigDecimal paymentFee,
		EPaymentMethod paymentMethod,
		EPaymentStatus paymentStatus) {}
