package com.watermelon.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.ProductDTO;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.service.SearchService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {

	SearchService searchService;
	
	@GetMapping
    public ResponseData<PageResponse<List<ProductDTO>>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer[] brands,
            @RequestParam(required = false) Integer[] categories,
            @RequestParam(required = false) Integer[] sizes,
            @PageableDefault(page = 0, size = 3) 
			@SortDefaults(
					@SortDefault(direction = Sort.Direction.ASC, sort = {"name" })
					) Pageable pageable
    		) {
		PageResponse<List<ProductDTO>> data = 
        		searchService.findProductsByCriteria(
        				name, 
        				brands, 
        				categories, 
        				sizes, 
        				pageable.getPageNumber(),
        				pageable.getPageSize(),
        				pageable.getSort());
		log.info(pageable.toString());
		
        return new ResponseData<>(HttpStatus.OK.value(), "Data products" ,data);
    }
	
}
