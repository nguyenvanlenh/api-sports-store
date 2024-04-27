package com.watermelon.mapper.imp;

import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.OrderAddressResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.OrderAddress;

public class OrderAdressMapper implements EntityMapper<OrderAddressResponse, OrderAddress>{

	@Override
	public OrderAddressResponse toDTO(OrderAddress entity) {
		if(ObjectUtils.isEmpty(entity)) return null;
		return new OrderAddressResponse(
				entity.getAddressLine1(),
				entity.getAddressLine2(),
				entity.getCity(),
				entity.getDistrict(),
				entity.getProvince(),
				entity.getCountry());
	}

	
}
