package com.watermelon.mapper.imp;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.ImageResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Image;
@Component
public class ImageMapper implements EntityMapper<ImageResponse, Image> {

	public ImageResponse toDTO(Image entity) {
		return Optional.ofNullable(entity)
				.map(img -> ImageResponse.builder()
						.id(img.getId())
						.path(img.getPath())
						.build())
				.orElse(ImageResponse.builder().build());
	}

}
