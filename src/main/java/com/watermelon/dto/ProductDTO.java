package com.watermelon.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;

@Builder
public record ProductDTO(
		Long id,
		String name, 
		String shortDescription,
		String description,
		BigDecimal price,
		Double tax,
		BrandDTO brand,
		CategoryDTO category,
		List<ImageDTO> listImages,
		List<SizeDTO> listSize) 
		{

}
