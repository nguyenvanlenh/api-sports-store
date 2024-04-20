package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.dto.ProductDTO;
import com.watermelon.dto.request.ProductRequest;
import com.watermelon.dto.response.PageResponse;

public interface ProductService {
	
	ProductDTO getProductById(Long id);
	PageResponse<List<ProductDTO>> getAllProduct(Pageable pageable);
	PageResponse<List<ProductDTO>> getProductContainName(String keyword, Pageable pageable);
	PageResponse<List<ProductDTO>> getProductByUrlKeyCategory(String urlKey, Pageable pageable);
	
	boolean deleteProduct(Long id);
	ProductDTO updateProduct(ProductDTO productDTO,List<MultipartFile> files);
	ProductDTO updateProduct(ProductDTO productDTO);
	ProductDTO addProduct(ProductRequest productRequest,List<MultipartFile> files);
	
	void updateProductQuantityForSize(int quantitySubtract, Long idProduct, Integer idSize);

}
