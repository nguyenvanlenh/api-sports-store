package com.watermelon.model.dto;

import com.watermelon.model.dto.mapper.EntityMapper;
import com.watermelon.model.entity.Category;

public record CategoryDTO(int id, String name)implements EntityMapper<CategoryDTO, Category>{

	@Override
	public CategoryDTO toDTO(Category entity) {
		if(entity == null) {
			return null;
		}
		return new CategoryDTO(entity.getId(), entity.getName());
	}
}