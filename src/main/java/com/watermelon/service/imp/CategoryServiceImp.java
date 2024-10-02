package com.watermelon.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.watermelon.dto.request.CategoryRequest;
import com.watermelon.dto.response.CategoryResponse;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.mapper.imp.CategoryMapper;
import com.watermelon.model.entity.Category;
import com.watermelon.repository.CategoryRepository;
import com.watermelon.service.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImp implements CategoryService{
	
	CategoryRepository categoryRepository;
	CategoryMapper categoryMapper;
	
	@Override
	public List<CategoryResponse> getAllCategories() {
		return categoryMapper.toDTO(categoryRepository.findAll());
	}
	@Override
	public Integer createCategory(CategoryRequest request) {
		Category category = categoryMapper.toEntity(request);
		return categoryRepository.save(category).getId();
	}
	@Override
	public void updateCategory(Integer cateId, CategoryRequest request) {
		Category category = categoryRepository.findById(cateId)
				.orElseThrow(() -> new ResourceNotFoundException("Category id {} not found", cateId));
		category.setName(request.name());
		category.setDescription(request.description());
		category.setActive(request.active());
		
		categoryRepository.save(category);
	}
	@Override
	public void deleteCategory(Integer cateId) {
		categoryRepository.deleteById(cateId);
	}
	
	
	

}
