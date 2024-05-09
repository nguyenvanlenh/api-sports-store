package com.watermelon.dto.response;

import java.util.List;

import com.watermelon.dto.response.OrderResponse.UserResponse;

public record OrderResponse(
		UserResponse user,
		OrderAddressResponse address,
		String note,
		Double tax,
		Double discount,
		Double totalPrice,
		Double deliveryFee,
		OrderStatusResponse orderStatus,
		DeliveryStatusResponse deliveryStatus,
		DeliveryMethodResponse deliveryMethod,
		String coupondCode,
		String rejectReason,
		List<OrderDetailResponse> listOrderDetails
		){
	public record UserResponse(
			Long id,
			String username) {
		
	}
}
