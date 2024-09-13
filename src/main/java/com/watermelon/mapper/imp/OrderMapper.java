package com.watermelon.mapper.imp;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.OrderResponse;
import com.watermelon.dto.response.UserResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderMapper implements EntityMapper<OrderResponse, Order> {
	OrderAdressMapper orderAdressMapper;
	OrderDetailMapper orderDetailMapper;
	
	@Override
	public OrderResponse toDTO(Order entity) {
		if (ObjectUtils.isEmpty(entity))
			return null;
		return new OrderResponse(
				entity.getId(), 
				UserResponse.builder()
					.id(entity.getUser().getId())
					.username(entity.getUser().getUsername())
					.build(),
				orderAdressMapper.toDTO(entity.getOrderAddress()), 
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
				orderDetailMapper.toDTO(entity.getListDetails().stream().toList()));
	}

}
