package com.watermelon.mapper.imp;

import com.watermelon.dto.response.PaymentResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Payment;

public class PaymentMapper implements EntityMapper<PaymentResponse,Payment>{
	
	private static PaymentMapper INSTANCE;
	
	private PaymentMapper() {}
	
	public static PaymentMapper getInstance() {
		if(INSTANCE == null) 
			synchronized (PaymentMapper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PaymentMapper();
                }
            }
		return INSTANCE;
	}

	@Override
	public PaymentResponse toDTO(Payment entity) {
		return new PaymentResponse(
				entity.getId(),
				OrderMapper.getInstance().toDTO(entity.getOrder()),
				entity.getAmount(),
				entity.getPaymentFee(),
				entity.getPaymentMethod(),
				entity.getPaymentStatus());
	}
	

}
