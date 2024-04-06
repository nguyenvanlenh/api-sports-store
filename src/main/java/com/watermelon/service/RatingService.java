package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.watermelon.dto.RatingDTO;
import com.watermelon.dto.request.RatingRequest;
import com.watermelon.dto.response.ResponsePageData;

public interface RatingService {
	ResponsePageData<List<RatingDTO>> getRatingListByProductId(Long productId, Pageable pageable);
	
	void addRating(RatingRequest rq);
	void deleteRating(Long id);
	Double caculatorAverageStar(Long id);
	
}
