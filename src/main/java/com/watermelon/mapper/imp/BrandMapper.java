package com.watermelon.mapper.imp;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.BrandResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Brand;
@Component
public class BrandMapper implements EntityMapper<BrandResponse, Brand> {

	@Override
	public BrandResponse toDTO(Brand entity) {
		if (entity == null) {
			return null;
		}
		return BrandResponse.builder()
				.id(entity.getId())
				.name(entity.getName())
				.isActive(entity.isActive())
				.build();
	}

}
