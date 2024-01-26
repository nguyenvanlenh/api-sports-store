package com.watermelon.service.mapper.imp;

import java.util.List;

import com.watermelon.model.entity.Image;
import com.watermelon.service.dto.ImageDTO;
import com.watermelon.service.mapper.EntityMapper;

public class ImageMapper implements EntityMapper<ImageDTO, Image>{

	public ImageDTO toDTO(Image img) {
		if(img== null) {
			return null;
		}
		return new ImageDTO(img.getId(), img.getPath());
	}
	
}
