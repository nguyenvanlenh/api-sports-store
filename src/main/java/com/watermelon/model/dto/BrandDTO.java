package com.watermelon.model.dto;

import com.watermelon.model.dto.mapper.EntityMapper;
import com.watermelon.model.entity.Brand;

public record BrandDTO(int id, String name) implements EntityMapper<BrandDTO, Brand> {

	@Override
	public BrandDTO toDTO(Brand entity) {
		if(entity == null) {
			return null;
		}
		return new BrandDTO(entity.getId(), entity.getName());
	}

	
}