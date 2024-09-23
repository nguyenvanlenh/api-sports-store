package com.watermelon.mapper.imp;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.OrderAddressResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.OrderAddress;
@Component
public class OrderAdressMapper implements EntityMapper<OrderAddressResponse, OrderAddress> {
	@Override
	public OrderAddressResponse toDTO(OrderAddress entity) {
		return Optional.ofNullable(entity)
				.map(oa -> OrderAddressResponse.builder()
						.addressLine(null)
						.commune(oa.getCommune())
						.district(oa.getDistrict())
						.province(oa.getProvince())
						.province(oa.getCountry())
						.build())
				.orElse(OrderAddressResponse.builder().build());
	}

}
