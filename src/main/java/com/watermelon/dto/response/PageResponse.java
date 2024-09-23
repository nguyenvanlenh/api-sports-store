package com.watermelon.dto.response;

import lombok.Builder;

@Builder
public record PageResponse<T>(
		int currentPage,
		int size,
		int totalPage,
		long totalElement,
		T content){
	
}
