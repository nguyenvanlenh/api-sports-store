package com.watermelon.service.mapper.imp;

import com.watermelon.model.entity.Rating;
import com.watermelon.service.dto.RatingDTO;
import com.watermelon.service.mapper.EntityMapper;


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
