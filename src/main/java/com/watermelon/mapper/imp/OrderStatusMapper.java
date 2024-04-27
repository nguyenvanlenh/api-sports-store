package com.watermelon.mapper.imp;

import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.OrderStatusResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.OrderStatus;

public class OrderStatusMapper implements EntityMapper<OrderStatusResponse, OrderStatus>{

	@Override
	public OrderStatusResponse toDTO(OrderStatus entity) {
		if(ObjectUtils.isEmpty(entity)) return null;
		return new OrderStatusResponse(entity.getId(), entity.getName());
	}

}
