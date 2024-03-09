package com.watermelon.service.dto;

import com.watermelon.model.entity.Brand;
import com.watermelon.service.mapper.EntityMapper;

public record BrandDTO(int id, String name) implements EntityMapper<BrandDTO, Brand> {

	@Override
	public BrandDTO toDTO(Brand entity) {
		if(entity == null) {
			return null;
		}
		return new BrandDTO(entity.getId(), entity.getName());
	}

	
}
