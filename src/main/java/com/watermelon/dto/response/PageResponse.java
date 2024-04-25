package com.watermelon.dto.response;

import java.io.Serializable;

public record PageResponse<T>(
		int currentPage,
		int size,
		int totalPage,
		long totalElement,
		T content) implements Serializable {
	
}
