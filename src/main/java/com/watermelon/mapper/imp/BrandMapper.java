package com.watermelon.mapper.imp;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.BrandResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Brand;
@Component
public class BrandMapper implements EntityMapper<BrandResponse, Brand> {
	@Override
	public BrandResponse toDTO(Brand entity) {
		return Optional.ofNullable(entity)
				.map(brand -> BrandResponse.builder()
					.id(entity.getId())
					.name(entity.getName())
					.isActive(entity.isActive())
					.build())
				.orElse(BrandResponse.builder().build());
	}

}
