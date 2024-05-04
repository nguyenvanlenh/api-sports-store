package com.watermelon.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
@Builder
public record ProductRequest(
        String name,
        String shortDescription,
        String description,
        BigDecimal price,
        Double tax,
        Integer idBrand,
        Integer idCategory,
        List<ProductSizeRequest> listSize
        ) implements Serializable {
}
