package com.watermelon.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.watermelon.dto.RatingDTO;
import com.watermelon.dto.mapper.imp.RatingMapper;
import com.watermelon.dto.request.RatingRequest;
import com.watermelon.dto.response.PaginationResponse;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.Rating;
import com.watermelon.model.entity.User;
import com.watermelon.repository.ProductRepository;
import com.watermelon.repository.RatingRepository;
import com.watermelon.repository.UserRepository;
import com.watermelon.service.RatingService;

@Service
public class RatingServiceImp implements RatingService{
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public PaginationResponse<List<RatingDTO>> getRatingListByProductId(Long productId, Pageable pageable) {
		Page<Rating> pageRating = ratingRepository.findByProduct_Id(productId, pageable);
		List<RatingDTO> listRatingDTO = new RatingMapper().toDTO(pageRating.getContent());
		
		PaginationResponse<List<RatingDTO>> result = new PaginationResponse<>(
				pageRating.getPageable().getPageNumber(),
				pageRating.getSize(), pageRating.getTotalPages(),
				pageRating.getTotalElements(),
				listRatingDTO);
		return result;
	}

	@Override
	public void addRating(RatingRequest request) {
		Rating rating = new Rating();
		
		rating.setContent(request.content());
		rating.setStar(request.star());
		Product product = productRepository.findById(request.productId())
				.orElseThrow(()-> new ResourceNotFoundException("PRODUCT_NOT_FOUND" ,request.productId()));
		rating.setProduct(product);
		
		User user = userRepository.findById(request.userId())
				.orElseThrow(()-> new ResourceNotFoundException("USER_NOT_FOUND",request.userId()));
		rating.setUser(user);
		
		ratingRepository.save(rating);
		
	}

	@Override
	public void deleteRating(Long id) {
		Rating rating = ratingRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("RATING_NOT_FOUND",id));
		ratingRepository.delete(rating);
		
	}

	@Override
	public Double caculatorAverageStar(Long id) {
		List<Object[]> totalStarAndRatings = ratingRepository.getTotalStarAndTotalRating(id);
		if(ObjectUtils.isEmpty(totalStarAndRatings.get(0)[0]))
			return 0.0;
		int totalStars = Integer.parseInt(totalStarAndRatings.get(0)[0].toString());
		int totalRatings = Integer.parseInt(totalStarAndRatings.get(0)[1].toString());
		
		Double averageStar = (totalStars * 1.0) /totalRatings;
		averageStar = Math.round(averageStar * 10.0) / 10.0;
		return averageStar;
	}

}
