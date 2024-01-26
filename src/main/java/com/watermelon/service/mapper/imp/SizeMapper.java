package com.watermelon.service.mapper.imp;

import java.util.List;

import com.watermelon.model.entity.ProductQuantity;
import com.watermelon.service.dto.SizeDTO;
import com.watermelon.service.mapper.EntityMapper;

public class SizeMapper implements EntityMapper<SizeDTO, ProductQuantity>{

	public SizeDTO toDTO(ProductQuantity productQuantity) {
		if(productQuantity == null) {
			return null;
		}
		return new SizeDTO(productQuantity.getSize().getId(),
				productQuantity.getSize().getName(),
				productQuantity.getQuantity());
	}
	
}
