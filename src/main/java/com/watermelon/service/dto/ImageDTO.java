package com.watermelon.service.dto;

import com.watermelon.model.entity.Image;
import com.watermelon.service.mapper.EntityMapper;

public record ImageDTO(long id, String path)implements EntityMapper<ImageDTO, Image>{

	public ImageDTO toDTO(Image img) {
		if(img== null) {
			return null;
		}
		return new ImageDTO(img.getId(), img.getPath());
	}
}
