package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.watermelon.model.request.RequestRating;
import com.watermelon.model.response.ResponsePageData;
import com.watermelon.service.dto.RatingDTO;

public interface RatingService {
	ResponsePageData<List<RatingDTO>> getRatingListByProductId(Long productId, Pageable pageable);
	
	void addRating(RequestRating rq);
	void deleteRating(Long id);
	Double caculatorAverageStar(Long id);
	
}
