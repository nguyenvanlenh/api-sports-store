package com.watermelon.viewandmodel.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponsePageData<T>(
		T content,
		@JsonProperty("current_page")
		int currentPage,
		int size,
		@JsonProperty("total_page")
		int totalPage,
		@JsonProperty("total_element")
		long totalElement) {

}
