package com.watermelon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.request.CategoryRequest;
import com.watermelon.dto.response.CategoryResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.service.CategoryService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/categories")
@Slf4j
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
	@PostMapping
	public ResponseData<Integer> createCategory(@Valid @RequestBody CategoryRequest request){
		return ResponseData.<Integer>builder()
				.status(HttpStatus.CREATED.value())
				.message("Category created successfully")
				.data(categoryService.createCategory(request))
				.build();
	}
	@PutMapping("/{categoryId}")
	public ResponseData<Void> updateCategory(@PathVariable Integer categoryId,
							@Valid @RequestBody CategoryRequest request){
		categoryService.updateCategory(categoryId, request);
		return ResponseData.<Void>builder()
				.status(HttpStatus.ACCEPTED.value())
				.message("Category updated successfully")
				.build();
	}
	@DeleteMapping("/{categoryId}")
	public ResponseData<Void> deleteCategory(@PathVariable Integer categoryId){
		categoryService.deleteCategory(categoryId);
		return ResponseData.<Void>builder()
				.status(HttpStatus.NO_CONTENT.value())
				.message("Category deleted successfully")
				.build();
	}

}
