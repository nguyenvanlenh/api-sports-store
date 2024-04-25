package com.watermelon.dto;

import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.ProductQuantity;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record SizeDTO(int id, String name, int quantity)implements EntityMapper<SizeDTO, ProductQuantity>, Serializable {

	public SizeDTO toDTO(ProductQuantity productQuantity) {
		if(productQuantity == null) {
			return null;
		}
		return new SizeDTO(productQuantity.getSize().getId(),
				productQuantity.getSize().getName(),
				productQuantity.getQuantity());
	}
}
