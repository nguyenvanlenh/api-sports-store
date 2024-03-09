package com.watermelon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.service.RatingService;
import com.watermelon.service.dto.RatingDTO;
import com.watermelon.viewandmodel.request.RequestRating;
import com.watermelon.viewandmodel.response.ResponseData;
import com.watermelon.viewandmodel.response.ResponsePageData;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

	
	@Autowired
	private RatingService ratingService;
	
	@GetMapping("/{productId}/average-star")
	public Double getTotal(@PathVariable Long productId){
		return ratingService.caculatorAverageStar(productId);
	}
	
	@GetMapping("/products/{productId}")
	public ResponseEntity<?> getRatingListByProductId(@PathVariable(name = "productId") Long id,
			@PageableDefault(page = 0, size = 20) @SortDefaults(@SortDefault(direction = Sort.Direction.DESC, sort = {
			"price" })) Pageable pageable
			){
		ResponsePageData<List<RatingDTO>> data = ratingService.getRatingListByProductId(id, pageable);
		return ResponseEntity.ok(new ResponseData(data, HttpStatus.OK.name(), HttpStatus.OK.getReasonPhrase()));
	}
	
	@PostMapping
	public ResponseEntity<Void> addRating(@RequestBody RequestRating requestRating){
		ratingService.addRating(requestRating);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRating(@PathVariable(name = "id") Long id){
		ratingService.deleteRating(id);
		return ResponseEntity.ok().build();
	}
}
