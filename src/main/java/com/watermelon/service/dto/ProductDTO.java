package com.watermelon.service.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductDTO(
		long id,
		String name,
//		@JsonProperty("short_description")
		String shortDescription,
		String description,
		String gtin,
		String skug,
		String slug,
		BigDecimal price,
		double tax,
		BrandDTO brand,
		CategoryDTO category,
//		@JsonProperty("list_images")
		List<ImageDTO> listImages,
//		@JsonProperty("list_size")
		List<SizeDTO> listSize
		) {

}
