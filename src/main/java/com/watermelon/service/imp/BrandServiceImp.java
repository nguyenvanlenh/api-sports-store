package com.watermelon.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.watermelon.dto.request.BrandRequest;
import com.watermelon.dto.response.BrandResponse;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.mapper.imp.BrandMapper;
import com.watermelon.model.entity.Brand;
import com.watermelon.repository.BrandRepository;
import com.watermelon.service.BrandService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImp implements BrandService {

	BrandRepository brandRepository;
	BrandMapper brandMapper;

	@Override
	public List<BrandResponse> getAllBrands() {
		return brandMapper.toDTO(brandRepository.findAll());
	}

	@Override
	public Integer createBrand(BrandRequest request) {
		Brand brand = brandMapper.toEntity(request);
		return brandRepository.save(brand).getId();
	}

	@Override
	public void deleteBrand(Integer brandId) {
		brandRepository.deleteById(brandId);
	}

	@Override
	public void updateBrand(Integer brandId, BrandRequest request) {
		Brand brand = brandRepository.findById(brandId)
				.orElseThrow(() -> new ResourceNotFoundException("Brand id {} not found", brandId));
		brand.setName(request.name());
		brand.setDescription(request.description());
		brand.setActive(request.active());

		brandRepository.save(brand);
	}

}
