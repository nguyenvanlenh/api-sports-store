package com.watermelon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.BrandDTO;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.model.entity.Size;
import com.watermelon.service.BrandService;
import com.watermelon.service.SizeService;

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
	public ResponseData<List<BrandDTO>> getAllCategories(){
		return new ResponseData<List<BrandDTO>>(200, "Data brands", brandService.getAllBrands());
	}

}
