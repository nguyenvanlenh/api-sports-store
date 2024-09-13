package com.watermelon.mapper.imp;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.OrderDetailResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.OrderDetail;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailMapper implements EntityMapper<OrderDetailResponse, OrderDetail> {
	ProductMapper productMapper;
	
	@Override
	public OrderDetailResponse toDTO(OrderDetail entity) {
		if (ObjectUtils.isEmpty(entity))
			return null;
		return new OrderDetailResponse(
				entity.getId(),
				productMapper.toDTO(entity.getProduct()), 
				entity.getQuantity(),
				entity.getPrice(), 
				entity.getDiscountAmount(), 
				entity.getSize(), 
				entity.getCategogy(),
				entity.getBrand(), 
				entity.getIsRating());
	}

}
