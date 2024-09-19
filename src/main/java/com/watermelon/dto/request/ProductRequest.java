package com.watermelon.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
@Builder
public record ProductRequest(
		 	@NotBlank(message = "Name must not be blank")
		 	String name,
	        String shortDescription,
	        @NotBlank(message = "Description must not be blank")
		 	String description,
	        @NotNull(message = "Sale price must not be null")
		 	BigDecimal salePrice,
	        @NotNull(message = "Regular price not be null")
		 	BigDecimal regularPrice,
	        @NotNull(message = "Brand ID must not be null")
		 	Integer brandId,
	        @NotNull(message = "Category ID must not be null")
		 	Integer categoryId,
		 	List<ProductImageRequest> listImages,
	        List<@Valid ProductSizeRequest> listSizes
        ) {
}
