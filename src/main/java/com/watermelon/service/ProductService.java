package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.model.request.ProductRequest;
import com.watermelon.model.response.ResponsePageData;
import com.watermelon.service.dto.ProductDTO;

public interface ProductService {
	
	ProductDTO getProductById(Long id);
	ResponsePageData<List<ProductDTO>> getAllProduct(Pageable pageable);
	ResponsePageData<List<ProductDTO>> getProductContainName(String keyword, Pageable pageable);
	ResponsePageData<List<ProductDTO>> getProductByUrlKeyCategory(String urlKey, Pageable pageable);
	
	boolean deleteProduct(Long id);
	ProductDTO updateProduct(ProductDTO productDTO,List<MultipartFile> files);
	ProductDTO updateProduct(ProductDTO productDTO);
	ProductDTO addProduct(ProductRequest productRequest,List<MultipartFile> files);
	
	void updateProductQuantityForSize(int quantitySubtract, Long idProduct, Integer idSize);

}
