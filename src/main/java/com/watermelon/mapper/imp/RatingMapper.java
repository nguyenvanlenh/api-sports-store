package com.watermelon.mapper.imp;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.RatingResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Rating;

@Component
public class RatingMapper implements EntityMapper<RatingResponse, Rating>{
	
	@Override
	public RatingResponse toDTO(Rating entity) {
		return Optional.ofNullable(entity)
				.map(rating -> RatingResponse.builder()
					.id(rating.getId())
					.content(rating.getContent())
					.star(rating.getStar())
					.urlAvatar(rating.getUser().getAvatar())
					.nameCustomer(rating.getUser().getEmail())
					.createdOn(rating.getCreatedOn())
					.build())
				.orElse(RatingResponse.builder().build());
	}

}
