package com.watermelon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.CategoryDTO;
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
	public ResponseData<List<CategoryDTO>> getAllCategories(){
		return new ResponseData<List<CategoryDTO>>(200, "Data categories", categoryService.getAllCategories());
	}

}
