package com.watermelon.mapper.imp;

import com.watermelon.dto.CategoryDTO;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Category;

public class CategoryMapper implements EntityMapper<CategoryDTO, Category>{

	@Override
	public CategoryDTO toDTO(Category entity) {
		if(entity == null) {
			return null;
		}
		return new CategoryDTO(entity.getId(), entity.getName());
	}

}
