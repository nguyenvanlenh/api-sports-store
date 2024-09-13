package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ProductResponse;

public interface SearchService {

	PageResponse<List<ProductResponse>> findProductsByCriteria(
            String name,
            Integer[] brands,
            Integer[] categories,
            Integer[] sizes,
            int pageNo,
			int pageSize,
            Sort sort
            );
}
