package com.watermelon.model.request;

import java.util.Set;

public record OrderRequest(
		OrderAddressRequest address,
		String note,
		Double tax,
		Double discount,
		Double totalPrice,
		Double deliveryFee,
		Integer orderStatus,
		Integer deliveryStatus,
		Integer deliveryMethod,
		String coupondCode,
		String rejectReason,
		Set<OrderDetailRequest> listOrderDetails
		) {

}
