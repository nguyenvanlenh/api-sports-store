package com.watermelon.mapper.imp;

import org.springframework.util.StringUtils;

import com.watermelon.dto.response.RatingResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Rating;


public class RatingMapper implements EntityMapper<RatingResponse, Rating>{

	private static RatingMapper INSTANCE;

	private RatingMapper() {
	}

	public static RatingMapper getInstance() {
		if (INSTANCE == null)
			INSTANCE = new RatingMapper();
		return INSTANCE;
	}
	@Override
	public RatingResponse toDTO(Rating entity) {
		if (entity == null) {
			return null;
		}
		return new RatingResponse(entity.getId(),
				entity.getContent(),
				entity.getStar(), 
				entity.getUser().getEmail(),
				entity.getCreatedOn()
				);
	}

}
