package com.watermelon.dto;

import java.math.BigDecimal;
import java.util.List;

import com.watermelon.mapper.EntityMapper;
import com.watermelon.mapper.imp.BrandMapper;
import com.watermelon.mapper.imp.CategoryMapper;
import com.watermelon.mapper.imp.ImageMapper;
import com.watermelon.mapper.imp.SizeMapper;
import com.watermelon.model.entity.Product;

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
implements EntityMapper<ProductDTO, Product> {

	@Override
	public ProductDTO toDTO(Product entity) {
		if (entity == null) {
			return null;
		}
		return new ProductDTO(
				entity.getId(),
				entity.getName(),
				entity.getShortDescription(),
				entity.getDescription(),
				entity.getPrice(),
				entity.getTax(),
				new BrandMapper().toDTO(entity.getBrand()),
				new CategoryMapper().toDTO(entity.getCategory()),
				new ImageMapper().toDTO(entity.getListImages().stream().toList()),
				new SizeMapper().toDTO(entity.getQuantityOfSizes().stream().toList()));
	}
}
