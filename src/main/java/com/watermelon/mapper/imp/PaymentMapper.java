package com.watermelon.mapper.imp;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.PaymentResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Payment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentMapper implements EntityMapper<PaymentResponse, Payment> {

	OrderMapper orderMapper;

	@Override
	public PaymentResponse toDTO(Payment entity) {
		return new PaymentResponse(
				entity.getId(), 
				orderMapper.toDTO(entity.getOrder()),
				entity.getAmount(), 
				entity.getPaymentFee(), 
				entity.getPaymentMethod(), 
				entity.getPaymentStatus());
	}

}
