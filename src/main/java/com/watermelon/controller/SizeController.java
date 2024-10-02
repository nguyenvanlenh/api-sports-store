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

import com.watermelon.dto.request.SizeRequest;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.dto.response.SizeResponse;
import com.watermelon.service.SizeService;

import jakarta.validation.Valid;
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
		return ResponseData.<List<SizeResponse>>builder()
				.status(HttpStatus.OK.value())
				.message("Sizes data")
				.data(sizeService.getAllSizes())
				.build();
	}
	@PostMapping
	public ResponseData<Integer> createCategory(@Valid @RequestBody SizeRequest request){
		return ResponseData.<Integer>builder()
				.status(HttpStatus.CREATED.value())
				.message("Size created successfully")
				.data(sizeService.createSize(request))
				.build();
	}
	@PutMapping("/{sizeId}")
	public ResponseData<Void> updateCategory(@PathVariable Integer sizeId,
							@Valid @RequestBody SizeRequest request){
		sizeService.updateSize(sizeId, request);
		return ResponseData.<Void>builder()
				.status(HttpStatus.ACCEPTED.value())
				.message("Size updated successfully")
				.build();
	}
	@DeleteMapping("/{sizeId}")
	public ResponseData<Void> deleteCategory(@PathVariable Integer sizeId){
		sizeService.deleteSize(sizeId);
		return ResponseData.<Void>builder()
				.status(HttpStatus.NO_CONTENT.value())
				.message("Size deleted successfully")
				.build();
	}

}
