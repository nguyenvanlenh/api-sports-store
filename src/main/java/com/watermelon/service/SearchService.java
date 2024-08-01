package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.watermelon.dto.ProductDTO;
import com.watermelon.dto.response.PageResponse;

public interface SearchService {

	PageResponse<List<ProductDTO>> findProductsByCriteria(
            String name,
            Integer[] brands,
            Integer[] categories,
            Integer[] sizes,
            int pageNo,
			int pageSize,
            Sort sort
            );
}
