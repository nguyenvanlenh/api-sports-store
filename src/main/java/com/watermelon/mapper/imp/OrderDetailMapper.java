package com.watermelon.mapper.imp;

import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.OrderDetailResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.OrderDetail;

public class OrderDetailMapper implements EntityMapper<OrderDetailResponse, OrderDetail>{

	@Override
	public OrderDetailResponse toDTO(OrderDetail entity) {
		if (ObjectUtils.isEmpty(entity))return null;
		return new OrderDetailResponse(
				entity.getId(),
				entity.getQuantity(),
				entity.getPrice(),
				entity.getDiscountAmount(),
				entity.getSize(),
				entity.getCategogy(),
				entity.getBrand(),
				entity.getTaxPercent());
	}

}
