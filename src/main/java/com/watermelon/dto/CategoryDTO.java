package com.watermelon.dto;

import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Category;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record CategoryDTO(int id, String name)implements EntityMapper<CategoryDTO, Category>, Serializable {

	@Override
	public CategoryDTO toDTO(Category entity) {
		if(entity == null) {
			return null;
		}
		return new CategoryDTO(entity.getId(), entity.getName());
	}
}
