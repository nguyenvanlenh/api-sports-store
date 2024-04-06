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
import com.watermelon.dto.response.ResponsePageData;
import com.watermelon.exception.NotFoundException;
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
	public ResponsePageData<List<RatingDTO>> getRatingListByProductId(Long productId, Pageable pageable) {
		Page<Rating> pageRating = ratingRepository.findByProduct_Id(productId, pageable);
		List<RatingDTO> listRatingDTO = new RatingMapper().toDTO(pageRating.getContent());
		
		ResponsePageData<List<RatingDTO>> result = new ResponsePageData<>(listRatingDTO,
				pageRating.getPageable().getPageNumber(), pageRating.getSize(), pageRating.getTotalPages(),
				pageRating.getTotalElements());
		return result;
	}

	@Override
	public void addRating(RatingRequest rq) {
		Rating rating = new Rating();
		
		rating.setContent(rq.content());
		rating.setStar(rq.star());
		Product product = productRepository.findById(rq.productId())
				.orElseThrow(()-> new NotFoundException("product not found!"));
		rating.setProduct(product);
		
		User user = userRepository.findById(rq.userId())
				.orElseThrow(()-> new NotFoundException("user not found!"));
		rating.setUser(user);
		
		ratingRepository.save(rating);
		
	}

	@Override
	public void deleteRating(Long id) {
		Rating rating = ratingRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("rating not found!"));
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
