package com.watermelon.mapper.imp;
import org.springframework.stereotype.Component;

import com.watermelon.dto.response.ImageResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Image;
@Component
public class ImageMapper implements EntityMapper<ImageResponse, Image> {

	public ImageResponse toDTO(Image img) {
		if (img == null) {
			return null;
		}
		return new ImageResponse(img.getId(), img.getPath());
	}

}
