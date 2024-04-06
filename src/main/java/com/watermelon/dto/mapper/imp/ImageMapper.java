package com.watermelon.dto.mapper.imp;

import com.watermelon.dto.ImageDTO;
import com.watermelon.dto.mapper.EntityMapper;
import com.watermelon.model.entity.Image;

public class ImageMapper implements EntityMapper<ImageDTO, Image>{

	public ImageDTO toDTO(Image img) {
		if(img== null) {
			return null;
		}
		return new ImageDTO(img.getId(), img.getPath());
	}
	
}
