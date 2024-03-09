package com.watermelon.service.dto;

import com.watermelon.model.entity.Category;
import com.watermelon.service.mapper.EntityMapper;

public record CategoryDTO(int id, String name)implements EntityMapper<CategoryDTO, Category>{

	@Override
	public CategoryDTO toDTO(Category entity) {
		if(entity == null) {
			return null;
		}
		return new CategoryDTO(entity.getId(), entity.getName());
	}
}
