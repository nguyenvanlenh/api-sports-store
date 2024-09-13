package com.watermelon.service;

import java.util.List;

import com.watermelon.dto.response.CategoryResponse;

public interface CategoryService {
	List<CategoryResponse> getAllCategories();
}
