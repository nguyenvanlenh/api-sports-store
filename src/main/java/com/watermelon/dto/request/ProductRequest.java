package com.watermelon.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        String name,
        String shortDescription,
        String description,
        BigDecimal price,
        double tax,
        Integer idBrand,
        Integer idCategory,
        List<ProductSizeRequest> listSize
) {
}
