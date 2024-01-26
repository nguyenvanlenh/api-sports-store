package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.service.dto.ProductDTO;
import com.watermelon.viewandmodel.error.ResponsePageData;
import com.watermelon.viewandmodel.request.ProductRequest;

public interface ProductService {
	
	ProductDTO getProductById(Long id);
	ResponsePageData<List<ProductDTO>> getAllProduct(Pageable pageable);
	ResponsePageData<List<ProductDTO>> getProductContainName(String keyword, Pageable pageable);
	
	boolean deleteProduct(Long id);
	ProductDTO updateProduct(ProductDTO productDTO,List<MultipartFile> files);
	ProductDTO addProduct(ProductRequest productRequest,List<MultipartFile> files);
	ProductDTO addProduct(ProductRequest productRequest);

}
