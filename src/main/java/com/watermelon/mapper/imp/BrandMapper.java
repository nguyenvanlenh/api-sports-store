package com.watermelon.mapper.imp;

import com.watermelon.dto.BrandDTO;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Brand;

public class BrandMapper implements EntityMapper<BrandDTO, Brand> {
	private static BrandMapper INSTANCE;

	private BrandMapper() {
	}

	public static BrandMapper getInstance() {
		if (INSTANCE == null)
			INSTANCE = new BrandMapper();
		return INSTANCE;
	}

	@Override
	public BrandDTO toDTO(Brand entity) {
		if (entity == null) {
			return null;
		}
		return new BrandDTO(entity.getId(), entity.getName());
	}

}
