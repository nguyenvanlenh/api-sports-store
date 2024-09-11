package com.watermelon.mapper.imp;

import com.watermelon.dto.ProductDTO;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Product;

public class ProductMapper implements EntityMapper<ProductDTO, Product> {

private static ProductMapper INSTANCE;
	
	private ProductMapper() {}
	
	public static ProductMapper getInstance() {
		if(INSTANCE == null) 
                    INSTANCE = new ProductMapper();
		return INSTANCE;
	}
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
				BrandMapper.getInstance().toDTO(entity.getBrand()),
				CategoryMapper.getInstance().toDTO(entity.getCategory()),
				ImageMapper.getInstance().toDTO(entity.getListImages().stream().toList()),
				SizeMapper.getInstance().toDTO(entity.getQuantityOfSizes().stream().toList()));
	}

}
