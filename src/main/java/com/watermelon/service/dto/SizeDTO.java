package com.watermelon.service.dto;

import com.watermelon.model.entity.ProductQuantity;
import com.watermelon.service.mapper.EntityMapper;

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
