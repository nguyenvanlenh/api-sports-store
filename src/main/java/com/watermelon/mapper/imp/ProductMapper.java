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
		return new ProductResponse(
				entity.getId(),
				entity.getName(),
				entity.getShortDescription(),
				entity.getDescription(),
				entity.getPrice(),
				entity.getTax(),
				brandMapper.toDTO(entity.getBrand()),
				categoryMapper.toDTO(entity.getCategory()),
				imageMapper.toDTO(entity.getListImages().stream().toList()),
				sizeMapper.toDTO(entity.getQuantityOfSizes().stream().toList()));
	}

}
