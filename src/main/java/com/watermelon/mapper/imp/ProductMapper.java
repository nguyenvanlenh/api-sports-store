package com.watermelon.mapper.imp;


import java.util.Optional;

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
		return Optional.ofNullable(entity)
				.map(product -> ProductResponse.builder()
					.id(product.getId())
					.name(product.getName())
					.shortDescription(product.getShortDescription())
					.description(product.getDescription())
					.salePrice(product.getSalePrice())
					.regularPrice(product.getRegularPrice())
					.thumbnailImage(product.getThumbnailImage())
					.isActive(product.isActive())
					.brand(brandMapper.toDTO(product.getBrand()))
					.category(categoryMapper.toDTO(product.getCategory()))
					.listImages(imageMapper.toDTO(product.getListImages().stream().toList()))
					.listSize(sizeMapper.toDTO(product.getQuantityOfSizes().stream().toList()))
					.build())
				.orElse(ProductResponse.builder().build());
	}

}
