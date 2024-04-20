package com.watermelon.mapper.imp;

import com.watermelon.dto.BrandDTO;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Brand;

public class BrandMapper implements EntityMapper<BrandDTO, Brand> {

	@Override
	public BrandDTO toDTO(Brand entity) {
		if(entity == null) {
			return null;
		}
		return new BrandDTO(entity.getId(), entity.getName());
	}

}
