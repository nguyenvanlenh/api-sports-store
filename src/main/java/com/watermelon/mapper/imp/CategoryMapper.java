package com.watermelon.mapper.imp;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.CategoryResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Category;
@Component
public class CategoryMapper implements EntityMapper<CategoryResponse, Category> {


	@Override
	public CategoryResponse toDTO(Category entity) {
		if (entity == null) {
			return null;
		}
		return new CategoryResponse(entity.getId(), entity.getName());
	}

}
