package com.watermelon.service.mapper.imp;

import com.watermelon.model.entity.Category;
import com.watermelon.service.dto.CategoryDTO;
import com.watermelon.service.mapper.EntityMapper;

public class CategoryMapper implements EntityMapper<CategoryDTO, Category>{

	@Override
	public CategoryDTO toDTO(Category entity) {
		if(entity == null) {
			return null;
		}
		return new CategoryDTO(entity.getId(), entity.getName());
	}

}
