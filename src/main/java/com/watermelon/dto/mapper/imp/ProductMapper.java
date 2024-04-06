package com.watermelon.dto.mapper.imp;

import com.watermelon.dto.ProductDTO;
import com.watermelon.dto.mapper.EntityMapper;
import com.watermelon.model.entity.Product;

public class ProductMapper implements EntityMapper<ProductDTO, Product> {

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

//	@Override
//	public Product toEntity(ProductDTO dto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<Product> toEntity(List<ProductDTO> dtos) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
