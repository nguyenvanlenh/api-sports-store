package com.watermelon.mapper.imp;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.PaymentResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Payment;
@Component
public class PaymentMapper implements EntityMapper<PaymentResponse, Payment> {

	@Override
	public PaymentResponse toDTO(Payment entity) {
		return Optional.ofNullable(entity)
				.map(order -> PaymentResponse.builder()
						.paymentId(order.getId())
						.amount(order.getAmount())
						.paymentFee(order.getPaymentFee())
						.paymentMethod(order.getPaymentMethod())
						.paymentStatus(order.getPaymentStatus()).build())
				.orElse(PaymentResponse.builder().build());
	}

}
