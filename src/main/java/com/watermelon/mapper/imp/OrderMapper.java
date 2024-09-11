package com.watermelon.mapper.imp;

import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.OrderResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Order;

public class OrderMapper implements EntityMapper<OrderResponse, Order> {

	private static OrderMapper INSTANCE;

	private OrderMapper() {
	}

	public static OrderMapper getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OrderMapper();
		return INSTANCE;
	}

	@Override
	public OrderResponse toDTO(Order entity) {
		if (ObjectUtils.isEmpty(entity))
			return null;
		return new OrderResponse(
				entity.getId(), 
				UserMapper.getInstance().toResponse(entity.getUser()),
				OrderAdressMapper.getInstance().toDTO(entity.getOrderAddress()), 
				entity.getNameCustomer(),
				entity.getEmailCustomer(), 
				entity.getPhoneNumberCustomer(), 
				entity.getTotalPrice(),
				entity.getDeliveryFee(), 
				entity.getOrderStatus(), 
				entity.getDeliveryStatus(),
				entity.getDeliveryMethod(), 
				entity.getCouponCode(), 
				entity.getRejectReason(),
				OrderDetailMapper.getInstance().toDTO(entity.getListDetails().stream().toList()));
	}

}
