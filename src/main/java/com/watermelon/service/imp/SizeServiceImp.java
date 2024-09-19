package com.watermelon.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.watermelon.dto.response.SizeResponse;
import com.watermelon.mapper.imp.SizeMapper;
import com.watermelon.repository.SizeRepository;
import com.watermelon.service.SizeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SizeServiceImp implements SizeService{
	
	SizeRepository sizeRepository;
	SizeMapper sizeMapper;

	@Override
	public List<SizeResponse> getAllSizes() {
		return sizeMapper.toDTOFromSizes(sizeRepository.findAll());
	}

	
}
