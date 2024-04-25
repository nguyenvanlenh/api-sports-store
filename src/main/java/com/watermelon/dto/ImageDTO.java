package com.watermelon.dto;

import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Image;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ImageDTO(long id, String path)implements EntityMapper<ImageDTO, Image>, Serializable {

	public ImageDTO toDTO(Image img) {
		if(img== null) {
			return null;
		}
		return new ImageDTO(img.getId(), img.getPath());
	}
}
