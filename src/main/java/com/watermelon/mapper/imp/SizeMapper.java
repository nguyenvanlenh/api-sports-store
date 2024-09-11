package com.watermelon.mapper.imp;

import com.watermelon.dto.SizeDTO;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.ProductQuantity;

public class SizeMapper implements EntityMapper<SizeDTO, ProductQuantity>{

	private static SizeMapper INSTANCE;

	private SizeMapper() {
	}

	public static SizeMapper getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SizeMapper();
		return INSTANCE;
	}
	
	public SizeDTO toDTO(ProductQuantity productQuantity) {
		if(productQuantity == null) {
			return null;
		}
		return new SizeDTO(
				productQuantity.getSize().getId(),
				productQuantity.getSize().getName(),
				productQuantity.getQuantity());
	}
	
}
