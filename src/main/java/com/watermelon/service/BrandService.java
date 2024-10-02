package com.watermelon.service;

import java.util.List;

import com.watermelon.dto.request.BrandRequest;
import com.watermelon.dto.response.BrandResponse;

public interface BrandService {

	List<BrandResponse> getAllBrands();
	Integer createBrand(BrandRequest request);
	void updateBrand(Integer brandId, BrandRequest request);
	void deleteBrand(Integer Brand);
}
