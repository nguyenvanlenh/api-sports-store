package com.watermelon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.response.BrandResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.service.BrandService;

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

}
