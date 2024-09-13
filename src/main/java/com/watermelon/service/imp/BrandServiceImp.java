package com.watermelon.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.watermelon.dto.response.BrandResponse;
import com.watermelon.mapper.imp.BrandMapper;
import com.watermelon.repository.BrandRepository;
import com.watermelon.service.BrandService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImp implements BrandService{

	BrandRepository brandRepository;
	BrandMapper brandMapper;

	@Override
	public List<BrandResponse> getAllBrands() {
		return brandMapper.toDTO(brandRepository.findAll());
	}
	
}
