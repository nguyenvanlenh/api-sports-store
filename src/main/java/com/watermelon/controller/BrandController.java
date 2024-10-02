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

import com.watermelon.dto.request.BrandRequest;
import com.watermelon.dto.response.BrandResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.service.BrandService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/brands")
public class BrandController {
	
	BrandService brandService;
	
	@GetMapping
	public ResponseData<List<BrandResponse>> getAllCategories(){
		return ResponseData.<List<BrandResponse>>builder()
				.status(HttpStatus.OK.value())
				.message("Brands data")
				.data(brandService.getAllBrands())
				.build();
	}
	@PostMapping
	public ResponseData<Integer> createCategory(@Valid @RequestBody BrandRequest request){
		return ResponseData.<Integer>builder()
				.status(HttpStatus.CREATED.value())
				.message("Brand created successfully")
				.data(brandService.createBrand(request))
				.build();
	}
	@PutMapping("/{brandId}")
	public ResponseData<Void> updateCategory(@PathVariable Integer brandId,
							@Valid @RequestBody BrandRequest request){
		brandService.updateBrand(brandId, request);
		return ResponseData.<Void>builder()
				.status(HttpStatus.ACCEPTED.value())
				.message("Brand updated successfully")
				.build();
	}
	@DeleteMapping("/{brandId}")
	public ResponseData<Void> deleteCategory(@PathVariable Integer brandId){
		brandService.deleteBrand(brandId);
		return ResponseData.<Void>builder()
				.status(HttpStatus.NO_CONTENT.value())
				.message("Brand deleted successfully")
				.build();
	}

}
