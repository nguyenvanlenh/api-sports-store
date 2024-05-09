package com.watermelon.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
@Builder
public record ProductRequest(
		 	@NotBlank(message = "Name must not be blank") String name,
	        String shortDescription,
	        @NotBlank(message = "Description must not be blank")
		 	String description,
	        @NotNull(message = "Price must not be null")
		 	BigDecimal price,
	        @NotNull(message = "Tax must not be null")
		 	Double tax,
	        @NotNull(message = "Brand ID must not be null")
		 	Integer idBrand,
	        @NotNull(message = "Category ID must not be null")
		 	Integer idCategory,
	        List<@Valid ProductSizeRequest> listSize
        ) {
}
