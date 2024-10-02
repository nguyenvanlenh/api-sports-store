package com.watermelon.mapper.imp;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.request.CategoryRequest;
import com.watermelon.dto.response.CategoryResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Category;
@Component
public class CategoryMapper implements EntityMapper<CategoryResponse, Category> {
	@Override
	public CategoryResponse toDTO(Category entity) {
		return Optional.ofNullable(entity)
			 	.map(cate -> CategoryResponse.builder()
					.id(entity.getId())
					.name(entity.getName())
					.description(entity.getDescription())
					.isActive(entity.isActive())
					.build())
			 	.orElse(CategoryResponse.builder().build());
	}
	public Category toEntity(CategoryRequest request) {
		return Optional.ofNullable(request)
				.map(cateRq -> Category.builder()
						.name(cateRq.name())
						.description(cateRq.description())
						.isActive(cateRq.active())
						.build())
				.orElse(Category.builder().build());
	}

}
