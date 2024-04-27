package com.watermelon.dto.response;

import java.io.Serializable;
import java.util.List;

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
		) implements Serializable {
	public record UserResponse(
			Long id,
			String username) {
		
	}
}
