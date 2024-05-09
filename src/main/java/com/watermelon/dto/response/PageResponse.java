package com.watermelon.dto.response;

public record PageResponse<T>(
		int currentPage,
		int size,
		int totalPage,
		long totalElement,
		T content){
	
}
