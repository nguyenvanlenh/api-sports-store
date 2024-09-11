package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.watermelon.dto.request.RatingRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.RatingResponse;

public interface RatingService {
	PageResponse<List<RatingResponse>> getRatingListByProductId(Long productId, Pageable pageable);
	
	void addRating(RatingRequest rq);
	void deleteRating(Long id);
	Double caculatorAverageStar(Long id);
	
}
