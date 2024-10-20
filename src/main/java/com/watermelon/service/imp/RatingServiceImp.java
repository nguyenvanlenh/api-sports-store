package com.watermelon.service.imp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.watermelon.dto.request.RatingRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.RatingResponse;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.mapper.imp.RatingMapper;
import com.watermelon.model.entity.OrderDetail;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.Rating;
import com.watermelon.model.entity.User;
import com.watermelon.repository.OrderDetailRepository;
import com.watermelon.repository.RatingRepository;
import com.watermelon.service.CommonService;
import com.watermelon.service.RatingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RatingServiceImp implements RatingService {

	RatingRepository ratingRepository;
	CommonService commonService;
	OrderDetailRepository orderDetailRepository;
	RatingMapper ratingMapper;

	@Override
	public PageResponse<List<RatingResponse>> getRatingListByProductId(Long productId, Pageable pageable) {
		Page<Rating> pageRating = ratingRepository.findByProductId(productId, pageable);
		List<RatingResponse> listRatingResponse = ratingMapper.toDTO(pageRating.getContent());

		return new PageResponse<>(pageRating.getPageable().getPageNumber(),
				pageRating.getSize(), pageRating.getTotalPages(), pageRating.getTotalElements(), listRatingResponse);
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
		
		OrderDetail orderDetail = findOrderDetailById(request.orderDetailId());
		orderDetail.setIsRating(true);
		orderDetailRepository.save(orderDetail);
		
		ratingRepository.save(rating);
		log.info("Add new rating for product with ID: {}", request.productId());
	}

	@Override
	public void deleteRating(Long id) {
		ratingRepository.deleteById(id);
		log.info("Delete rating with ID: {}", id);
	}

	@Override
	public Double caculatorAverageStar(Long id) {
		log.info("Calculating average star for product with ID: {}", id);
		List<Object[]> totalStarAndRatings = ratingRepository.getTotalStarAndTotalRating(id);
		if (ObjectUtils.isEmpty(totalStarAndRatings.get(0)[0]))
			return 0.0;
		int totalStars = Integer.parseInt(totalStarAndRatings.get(0)[0].toString());
		int totalRatings = Integer.parseInt(totalStarAndRatings.get(0)[1].toString());

		double averageStar = (totalStars * 1.0) / totalRatings;
		averageStar = Math.round(averageStar * 10.0) / 10.0;
		return averageStar;
	}
	
	private OrderDetail findOrderDetailById(Long orderDetailId) {
		return orderDetailRepository.findById(orderDetailId)
				.orElseThrow(()-> new ResourceNotFoundException("ORDER_DETAIL_NOT_FOUND", orderDetailId));
	}

}
