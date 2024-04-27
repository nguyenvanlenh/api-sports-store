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
				entity.getNote(),
				entity.getTax(),
				entity.getDiscount(),
				entity.getTotalPrice(),
				entity.getDeliveryFee(),
				new OrderStatusMapper().toDTO(entity.getOrderStatus()),
				new DeliveryStatusMapper().toDTO(entity.getDeliveryStatus()),
				new DeliveryMethodMapper().toDTO(entity.getDeliveryMethod()),
				entity.getCouponCode(),
				entity.getRejectReason(),
				new OrderDetailMapper().toDTO(entity.getListDetails().stream().toList()));
	}

	
}
