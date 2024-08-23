package com.watermelon.mapper.imp;

import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.OrderResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Order;

public class OrderMapper implements EntityMapper<OrderResponse, Order>{

	@Override
	public OrderResponse toDTO(Order entity) {
		if(ObjectUtils.isEmpty(entity)) return null;
		return new OrderResponse(
				new UserMapper().toResponse(entity.getUser()),
				new OrderAdressMapper().toDTO(entity.getOrderAddress()),
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
				new OrderDetailMapper().toDTO(entity.getListDetails().stream().toList()));
	}

	
}
