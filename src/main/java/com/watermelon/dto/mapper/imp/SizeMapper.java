package com.watermelon.dto.mapper.imp;

import com.watermelon.dto.SizeDTO;
import com.watermelon.dto.mapper.EntityMapper;
import com.watermelon.model.entity.ProductQuantity;

public class SizeMapper implements EntityMapper<SizeDTO, ProductQuantity>{

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
