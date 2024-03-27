package com.watermelon.model.dto;

import java.time.ZonedDateTime;

import com.watermelon.model.dto.mapper.EntityMapper;
import com.watermelon.model.entity.Rating;

public record RatingDTO(
		Long id,
		String content,
		int star,
		Long productId,
		Long userId,
		ZonedDateTime createdOn) implements EntityMapper<RatingDTO, Rating>{

	@Override
	public RatingDTO toDTO(Rating entity) {
		if (entity == null) {
			return null;
		}
		return new RatingDTO(entity.getId(),
				entity.getContent(),
				entity.getStar(), 
				entity.getProduct().getId(),
				entity.getUser().getId(),
				entity.getCreatedOn()
				);
	}
}
