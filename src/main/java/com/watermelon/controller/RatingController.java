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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.model.dto.RatingDTO;
import com.watermelon.model.request.RatingRequest;
import com.watermelon.model.response.ResponseData;
import com.watermelon.model.response.ResponsePageData;
import com.watermelon.service.RatingService;

@RestController
@RequestMapping("/api/rating")

public class RatingController {

	
 RatingService ratingService;
	
	@GetMapping("/{productId}/average-star")
	@ResponseStatus(HttpStatus.OK)
	public Double getTotal(@PathVariable Long productId){
		return ratingService.caculatorAverageStar(productId);
	}
	
	@GetMapping("/products/{productId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseData getRatingListByProductId(@PathVariable(name = "productId") Long id,
			@PageableDefault(page = 0, size = 20) Pageable pageable
			){
		ResponsePageData<List<RatingDTO>> data = ratingService.getRatingListByProductId(id, pageable);
		return new ResponseData(data, HttpStatus.OK.name(), HttpStatus.OK.getReasonPhrase());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addRating(@RequestBody RatingRequest requestRating){
		ratingService.addRating(requestRating);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteRating(@PathVariable(name = "id") Long id){
		ratingService.deleteRating(id);
	}
}
