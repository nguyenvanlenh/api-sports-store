package com.watermelon.service.imp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.watermelon.dto.RatingDTO;
import com.watermelon.dto.request.RatingRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.mapper.imp.RatingMapper;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.Rating;
import com.watermelon.model.entity.User;
import com.watermelon.repository.RatingRepository;
import com.watermelon.service.CommonService;
import com.watermelon.service.RatingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingServiceImp implements RatingService {

	RatingRepository ratingRepository;
	CommonService commonService;

	@Override
	public PageResponse<List<RatingDTO>> getRatingListByProductId(Long productId, Pageable pageable) {
		Page<Rating> pageRating = ratingRepository.findByProduct_Id(productId, pageable);
		List<RatingDTO> listRatingDTO = new RatingMapper().toDTO(pageRating.getContent());

		return new PageResponse<>(pageRating.getPageable().getPageNumber(),
				pageRating.getSize(), pageRating.getTotalPages(), pageRating.getTotalElements(), listRatingDTO);
	}

	@Override
	public void addRating(RatingRequest request) {
		Rating rating = new Rating();

		rating.setContent(request.content());
		rating.setStar(request.star());
		Product product = commonService.findProductById(request.productId());
		rating.setProduct(product);
		User user = commonService.findUserById(request.userId());
		rating.setUser(user);

		ratingRepository.save(rating);

	}

	@Override
	public void deleteRating(Long id) {
		ratingRepository.deleteById(id);

	}

	@Override
	public Double caculatorAverageStar(Long id) {
		List<Object[]> totalStarAndRatings = ratingRepository.getTotalStarAndTotalRating(id);
		if (ObjectUtils.isEmpty(totalStarAndRatings.get(0)[0]))
			return 0.0;
		int totalStars = Integer.parseInt(totalStarAndRatings.get(0)[0].toString());
		int totalRatings = Integer.parseInt(totalStarAndRatings.get(0)[1].toString());

		double averageStar = (totalStars * 1.0) / totalRatings;
		averageStar = Math.round(averageStar * 10.0) / 10.0;
		return averageStar;
	}

}
