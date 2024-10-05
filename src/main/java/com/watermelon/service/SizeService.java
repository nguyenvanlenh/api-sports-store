package com.watermelon.service;

import java.util.List;

import com.watermelon.dto.request.SizeRequest;
import com.watermelon.dto.response.SizeResponse;

public interface SizeService {
	List<SizeResponse> getAllSizes();
	Integer createSize(SizeRequest request);
	void updateSize(Integer sizeId, SizeRequest request);
	void deleteSize(Integer sizeId);
}
