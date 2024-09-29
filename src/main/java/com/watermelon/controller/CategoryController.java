package com.watermelon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.response.CategoryResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.service.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/categories")
public class CategoryController {
	
	CategoryService categoryService;
	
	@GetMapping
	public ResponseData<List<CategoryResponse>> getAllCategories(){
		return ResponseData.<List<CategoryResponse>>builder()
				.status(HttpStatus.OK.value())
				.message("Categories data")
				.data(categoryService.getAllCategories())
				.build();
	}

}
