package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.dto.request.ProductRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ProductResponse;

public interface ProductService {
	
	ProductResponse getProductById(Long id);
	PageResponse<List<ProductResponse>> getAllProduct(Pageable pageable);
	PageResponse<List<ProductResponse>> getProductContainName(String keyword, Pageable pageable);
	
	boolean deleteProduct(Long id);
	boolean updateProduct(Long idProduct, ProductRequest request,List<MultipartFile> files);
	Long addProduct(ProductRequest productRequest,List<MultipartFile> files);
	void updateProductQuantityForSize(int quantitySubtract, Long idProduct, Integer idSize);
	void updateProductStatus(Long idProduct, Boolean isActive);

}
