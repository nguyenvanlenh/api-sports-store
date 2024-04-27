package com.watermelon.mapper.imp;

import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.DeliveryMethodResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.DeliveryMethod;

public class DeliveryMethodMapper implements EntityMapper<DeliveryMethodResponse, DeliveryMethod>{

	@Override
	public DeliveryMethodResponse toDTO(DeliveryMethod entity) {
		if(ObjectUtils.isEmpty(entity)) return null;
		return new DeliveryMethodResponse(entity.getId(), entity.getName());
	}

}
