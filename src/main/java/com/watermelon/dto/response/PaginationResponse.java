package com.watermelon.dto.response;

public record PaginationResponse<T>(
		int currentPage,
		int size,
		int totalPage,
		long totalElement,
		T content) {
	
}
