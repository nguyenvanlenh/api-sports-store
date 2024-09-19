package com.watermelon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.response.ResponseData;
import com.watermelon.dto.response.SizeResponse;
import com.watermelon.service.SizeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/sizes")
public class SizeController {
	
	SizeService sizeService;
	
	@GetMapping
	public ResponseData<List<SizeResponse>> getAllCategories(){
		return new ResponseData<List<SizeResponse>>(200, "Data sizes", sizeService.getAllSizes());
	}

}
