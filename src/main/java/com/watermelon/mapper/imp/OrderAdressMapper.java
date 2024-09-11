package com.watermelon.mapper.imp;

import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.OrderAddressResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.OrderAddress;

public class OrderAdressMapper implements EntityMapper<OrderAddressResponse, OrderAddress> {
	private static OrderAdressMapper INSTANCE;

	private OrderAdressMapper() {
	}

	public static OrderAdressMapper getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OrderAdressMapper();
		return INSTANCE;
	}

	@Override
	public OrderAddressResponse toDTO(OrderAddress entity) {
		if (ObjectUtils.isEmpty(entity))
			return null;
		return new OrderAddressResponse(
				entity.getAddressLine(), 
				entity.getCommune(), 
				entity.getDistrict(),
				entity.getProvince(),
				entity.getCountry());
	}

}
