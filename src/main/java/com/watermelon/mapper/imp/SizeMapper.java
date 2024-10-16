package com.watermelon.mapper.imp;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.request.BrandRequest;
import com.watermelon.dto.request.SizeRequest;
import com.watermelon.dto.response.SizeResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Brand;
import com.watermelon.model.entity.ProductQuantity;
import com.watermelon.model.entity.Size;
@Component
public class SizeMapper implements EntityMapper<SizeResponse, ProductQuantity>{
	
	public SizeResponse toDTO(ProductQuantity entity) {
		return Optional.ofNullable(entity)
				.map(productQuantity -> SizeResponse.builder()
					.id(productQuantity.getSize().getId())
					.name(productQuantity.getSize().getName())
					.quantity(productQuantity.getQuantity())
					.description(productQuantity.getSize().getDescription())
					.isActive(productQuantity.getSize().isActive())
					.build())
				.orElse(SizeResponse.builder().build());
	}
	public SizeResponse toDTO(Size entity) {
		return Optional.ofNullable(entity)
				.map(size -> SizeResponse.builder()
					.id(size.getId())
					.name(size.getName())
					.description(size.getDescription())
					.isActive(size.isActive())
					.build())
				.orElse(SizeResponse.builder().build());
				
	}
	
	public Size toEntity(SizeRequest request) {
		return Optional.ofNullable(request)
				.map(sizeRq -> Size.builder()
						.name(sizeRq.name())
						.description(sizeRq.description())
						.isActive(sizeRq.active())
						.build())
				.orElse(Size.builder().build());
	}
	
	public List<SizeResponse> toDTOFromSizes(List<Size> sizes) {
		return Optional.ofNullable(sizes)
				.map(list ->list.stream().map(this::toDTO).toList())
				.orElse(Collections.emptyList());
	}
}
