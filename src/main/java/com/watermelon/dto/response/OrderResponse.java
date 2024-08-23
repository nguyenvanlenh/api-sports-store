package com.watermelon.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.watermelon.model.enumeration.EDeliveryMethod;
import com.watermelon.model.enumeration.EDeliveryStatus;
import com.watermelon.model.enumeration.EOrderStatus;

public record OrderResponse(
		UserResponse user,
		OrderAddressResponse address,
		String nameCustomer,
		String emailCustomer,
		String phoneNumberCustomer,
		BigDecimal totalPrice,
		BigDecimal deliveryFee,
		EOrderStatus orderStatus,
		EDeliveryStatus deliveryStatus,
		EDeliveryMethod deliveryMethod,
		String coupondCode,
		String rejectReason,
		List<OrderDetailResponse> listOrderDetails
		){
	public record UserResponse(
			Long id,
			String username) {
		
	}
}
