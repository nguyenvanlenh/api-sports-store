package com.watermelon.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.watermelon.dto.response.CategoryResponse;
import com.watermelon.mapper.imp.CategoryMapper;
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
	
	

}
