package com.watermelon.mapper.imp;


import org.springframework.stereotype.Component;

import com.watermelon.dto.response.ProductResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Product;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductMapper implements EntityMapper<ProductResponse, Product> {
	
	BrandMapper brandMapper;
	CategoryMapper categoryMapper;
	ImageMapper imageMapper;
	SizeMapper sizeMapper;
	
	@Override
	public ProductResponse toDTO(Product entity) {
		if (entity == null) {
			return null;
		}
		return ProductResponse.builder()
				.id(entity.getId())
				.name(entity.getName())
				.shortDescription(entity.getShortDescription())
				.description(entity.getDescription())
				.salePrice(entity.getSalePrice())
				.regularPrice(entity.getRegularPrice())
				.thumbnailImage(entity.getThumbnailImage())
				.isActive(entity.isActive())
				.brand(brandMapper.toDTO(entity.getBrand()))
				.category(categoryMapper.toDTO(entity.getCategory()))
				.listImages(imageMapper.toDTO(entity.getListImages().stream().toList()))
				.listSize(sizeMapper.toDTO(entity.getQuantityOfSizes().stream().toList()))
				.build();
	}

}
