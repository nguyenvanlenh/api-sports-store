package com.watermelon.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.request.RatingRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.RatingResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.service.RatingService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {

	
 	RatingService ratingService;
	
	@GetMapping("/products/{productId}/average-star")
	public ResponseData<Double> getAverageStar(
			@PathVariable Long productId){
		return ResponseData.<Double>builder()
				.status(HttpStatus.OK.value())
				.message(String.format("Average star of product %d", productId))
				.data(ratingService.caculatorAverageStar(productId))
				.build();
	}
	
	@GetMapping("/products/{productId}")
	public ResponseData<PageResponse<List<RatingResponse>>> getRatingListByProductId(
			@PathVariable(name = "productId") Long id,
			@PageableDefault(page = 0, size = 1) Pageable pageable){
		PageResponse<List<RatingResponse>> data = ratingService.getRatingListByProductId(id, pageable);
		return ResponseData.<PageResponse<List<RatingResponse>>>builder()
				.status(HttpStatus.OK.value())
				.message(String.format("List rating of product %d", id))
				.data(data)
				.build();
	}
	
	@PostMapping
	public ResponseData<Void> addRating(@Valid @RequestBody RatingRequest requestRating){
		ratingService.addRating(requestRating);
		return ResponseData.<Void>builder()
				.status(HttpStatus.CREATED.value())
				.message("Rating added successfully")
				.build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseData<Void> deleteRating(@PathVariable(name = "id") Long id){
		ratingService.deleteRating(id);
		return ResponseData.<Void>builder()
				.status(HttpStatus.NO_CONTENT.value())
				.message("Rating deleted successfully")
				.build();
	}
}
