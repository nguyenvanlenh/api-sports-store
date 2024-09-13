package com.watermelon.mapper.imp;

import org.springframework.util.StringUtils;

import com.watermelon.dto.response.RatingResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Rating;


public enum RatingMapper implements EntityMapper<RatingResponse, Rating>{

	INSTANCE;
	@Override
	public RatingResponse toDTO(Rating entity) {
		if (entity == null) {
			return null;
		}
		return new RatingResponse(entity.getId(),
				entity.getContent(),
				entity.getStar(),
				entity.getUser().getAvatar(),
				entity.getUser().getEmail(),
				entity.getCreatedOn()
				);
	}

}
