package com.watermelon.service.mapper.imp;

import java.util.List;

import com.watermelon.model.entity.Brand;
import com.watermelon.service.dto.BrandDTO;
import com.watermelon.service.mapper.EntityMapper;

public class BrandMapper implements EntityMapper<BrandDTO, Brand> {

	@Override
	public BrandDTO toDTO(Brand entity) {
		if(entity == null) {
			return null;
		}
		return new BrandDTO(entity.getId(), entity.getName());
	}
	

	

}
