package com.watermelon.mapper.imp;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.SizeResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.ProductQuantity;
@Component
public class SizeMapper implements EntityMapper<SizeResponse, ProductQuantity>{
	
	public SizeResponse toDTO(ProductQuantity productQuantity) {
		if(productQuantity == null) {
			return null;
		}
		return new SizeResponse(
				productQuantity.getSize().getId(),
				productQuantity.getSize().getName(),
				productQuantity.getQuantity());
	}
	
}
