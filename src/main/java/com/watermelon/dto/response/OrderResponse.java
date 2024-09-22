package com.watermelon.dto.response;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import com.watermelon.model.enumeration.EDeliveryMethod;
import com.watermelon.model.enumeration.EDeliveryStatus;
import com.watermelon.model.enumeration.EOrderStatus;

import lombok.Builder;
@Builder
public record OrderResponse(
		Long id,
		PaymentResponse payment,
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
		List<OrderDetailResponse> listOrderDetails,
		ZonedDateTime createdOn
		){
}
