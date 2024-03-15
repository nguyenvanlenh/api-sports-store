package com.watermelon.model.request;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        String name,
        String shortDescription,
        String description,
        String gtin,
        String sku,
        String slug,
        BigDecimal price,
        double tax,
        Integer idBrand,
        Integer idCategory,
        List<ProductSizeRequest> listSize
) {
}
