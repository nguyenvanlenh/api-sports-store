package com.watermelon.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.watermelon.exception.NotFoundException;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.Rating;
import com.watermelon.model.entity.User;
import com.watermelon.repository.ProductRepository;
import com.watermelon.repository.RatingRepository;
import com.watermelon.repository.UserRepository;
import com.watermelon.service.RatingService;
import com.watermelon.service.dto.ProductDTO;
import com.watermelon.service.dto.RatingDTO;
import com.watermelon.service.mapper.imp.RatingMapper;
import com.watermelon.viewandmodel.request.RequestRating;
import com.watermelon.viewandmodel.response.ResponsePageData;

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
	public void addRating(RequestRating rq) {
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
		
		return averageStar;
	}

}
