package com.watermelon.service.imp;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ProductResponse;
import com.watermelon.repository.SearchRepository;
import com.watermelon.service.SearchService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchServiceImp implements SearchService{

	SearchRepository searchRepository;
	
	@Override
	public PageResponse<List<ProductResponse>> findProductsByCriteria(String name, Integer[] brands,
			Integer[] categories, Integer[] sizes, int pageNo, int pageSize, Sort sort) {
		
		return searchRepository.findProductsByCriteria(name, brands, categories, sizes, pageNo, pageSize, sort);
	}
	

}
