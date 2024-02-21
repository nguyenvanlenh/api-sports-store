package com.watermelon.service.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductDTO(
		Long id,
		String name,
		String shortDescription,
		String description,
		String gtin,
		String skug,
		String slug,
		BigDecimal price,
		Double tax,
		BrandDTO brand,
		CategoryDTO category,
		List<ImageDTO> listImages,
		List<SizeDTO> listSize
		) {

}
