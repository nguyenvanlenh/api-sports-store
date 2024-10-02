package com.watermelon.service;

import java.util.List;

import com.watermelon.dto.request.CategoryRequest;
import com.watermelon.dto.response.CategoryResponse;

public interface CategoryService {
	List<CategoryResponse> getAllCategories();
	Integer createCategory(CategoryRequest request);
	void updateCategory(Integer cateId, CategoryRequest request);
	void deleteCategory(Integer cateId);
}
