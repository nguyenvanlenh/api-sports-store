package com.watermelon.model.dto;

import com.watermelon.model.dto.mapper.EntityMapper;
import com.watermelon.model.entity.Image;

public record ImageDTO(long id, String path)implements EntityMapper<ImageDTO, Image>{

	public ImageDTO toDTO(Image img) {
		if(img== null) {
			return null;
		}
		return new ImageDTO(img.getId(), img.getPath());
	}
}
