package com.watermelon.mapper.imp;

import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.OrderDetailResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.OrderDetail;

public class OrderDetailMapper implements EntityMapper<OrderDetailResponse, OrderDetail> {

	private static OrderDetailMapper INSTANCE;

	private OrderDetailMapper() {
	}

	public static OrderDetailMapper getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OrderDetailMapper();
		return INSTANCE;
	}

	@Override
	public OrderDetailResponse toDTO(OrderDetail entity) {
		if (ObjectUtils.isEmpty(entity))
			return null;
		return new OrderDetailResponse(
				entity.getId(),
				ProductMapper.getInstance().toDTO(entity.getProduct()), 
				entity.getQuantity(),
				entity.getPrice(), 
				entity.getDiscountAmount(), 
				entity.getSize(), 
				entity.getCategogy(),
				entity.getBrand(), 
				entity.getIsRating());
	}

}
