package com.watermelon.mapper.imp;

import com.watermelon.dto.ProductDTO;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Product;

public class ProductMapper implements EntityMapper<ProductDTO, Product> {

private static ProductMapper INSTANCE;
	
//	private PaymentMapper() {}
	
	public static ProductMapper getInstance() {
		if(INSTANCE == null) 
			synchronized (ProductMapper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProductMapper();
                }
            }
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
				new BrandMapper().toDTO(entity.getBrand()),
				new CategoryMapper().toDTO(entity.getCategory()),
				new ImageMapper().toDTO(entity.getListImages().stream().toList()),
				new SizeMapper().toDTO(entity.getQuantityOfSizes().stream().toList()));
	}

}
