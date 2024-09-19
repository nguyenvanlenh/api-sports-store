package com.watermelon.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;

@Builder
public record ProductResponse(
		Long id,
		String name, 
		String shortDescription,
		String description,
		BigDecimal salePrice,
		BigDecimal regularPrice,
		String thumbnailImage,
		Boolean isActive,
		BrandResponse brand,
		CategoryResponse category,
		List<ImageResponse> listImages,
		List<SizeResponse> listSize) 
		{

}
