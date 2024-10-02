package com.watermelon.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.watermelon.dto.request.SizeRequest;
import com.watermelon.dto.response.SizeResponse;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.mapper.imp.SizeMapper;
import com.watermelon.model.entity.Brand;
import com.watermelon.model.entity.Size;
import com.watermelon.repository.SizeRepository;
import com.watermelon.service.SizeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SizeServiceImp implements SizeService {

	SizeRepository sizeRepository;
	SizeMapper sizeMapper;

	@Override
	public List<SizeResponse> getAllSizes() {
		return sizeMapper.toDTOFromSizes(sizeRepository.findAll());
	}

	@Override
	public Integer createSize(SizeRequest request) {
		Size size = sizeMapper.toEntity(request);
		return sizeRepository.save(size).getId();
	}

	@Override
	public void deleteSize(Integer sizeId) {
		sizeRepository.deleteById(sizeId);

	}

	@Override
	public void updateSize(Integer sizeId, SizeRequest request) {
		Size size = sizeRepository.findById(sizeId)
				.orElseThrow(() -> new ResourceNotFoundException("Size id {} not found", sizeId));
		size.setName(request.name());
		size.setDescription(request.description());
		size.setActive(request.active());

		sizeRepository.save(size);

	}

}
