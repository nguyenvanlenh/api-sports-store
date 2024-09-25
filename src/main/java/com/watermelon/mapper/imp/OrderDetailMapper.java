package com.watermelon.mapper.imp;

import java.util.Optional;

import org.springframework.stereotype.Component;

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
		return Optional.ofNullable(entity)
				.map(od -> OrderDetailResponse.builder()
						.id(od.getId())
						.brand(od.getBrand())
						.categogy(od.getCategogy())
						.size(od.getSize())
						.quantity(od.getQuantity())
						.price(od.getPrice())
						.isRating(entity.getIsRating())
						.discountAmount(od.getDiscountAmount())
						.product(productMapper.toDTO(od.getProduct()))
						.build())
				.orElse(OrderDetailResponse.builder().build());

	}

}
