package com.watermelon.viewandmodel.request;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public record ProductRequest(
        String name,
        String shortDescription,
        String description,
        String gtin,
        String skug,
        String slug,
        BigDecimal price,
        double tax,
        Integer idBrand,
        Integer idCategory,
        List<ProductSizeRequest> listSize
) {
}
