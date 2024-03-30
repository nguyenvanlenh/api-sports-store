package com.watermelon.model.dto;

import com.watermelon.model.dto.mapper.EntityMapper;
import com.watermelon.model.entity.ProductQuantity;

public record SizeDTO(int id, String name, int quantity)implements EntityMapper<SizeDTO, ProductQuantity>{

	public SizeDTO toDTO(ProductQuantity productQuantity) {
		if(productQuantity == null) {
			return null;
		}
		return new SizeDTO(productQuantity.getSize().getId(),
				productQuantity.getSize().getName(),
				productQuantity.getQuantity());
	}
}