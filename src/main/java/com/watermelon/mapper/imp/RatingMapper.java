package com.watermelon.mapper.imp;

import com.watermelon.dto.RatingDTO;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Rating;


public class RatingMapper implements EntityMapper<RatingDTO, Rating>{

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
