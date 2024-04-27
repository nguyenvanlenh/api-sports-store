package com.watermelon.mapper.imp;

import org.springframework.util.ObjectUtils;

import com.watermelon.dto.response.DeliveryStatusResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.DeliveryStatus;

public class DeliveryStatusMapper implements EntityMapper<DeliveryStatusResponse, DeliveryStatus>{

	@Override
	public DeliveryStatusResponse toDTO(DeliveryStatus entity) {
		if(ObjectUtils.isEmpty(entity)) return null;
		return new DeliveryStatusResponse(entity.getId(), entity.getName());
	}

}
