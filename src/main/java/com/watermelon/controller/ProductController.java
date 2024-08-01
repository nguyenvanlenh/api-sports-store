package com.watermelon.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.dto.ProductDTO;
import com.watermelon.dto.request.ProductRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.service.ImageService;
import com.watermelon.service.ProductService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
	ProductService productService;
	ImageService imageService;

	@GetMapping
	public ResponseData<PageResponse<List<ProductDTO>>> getProducts(
			@PageableDefault(page = 0, size = 20) 
			@SortDefaults(
					@SortDefault(direction = Sort.Direction.DESC, sort = {"price" })
					) Pageable pageable,
			@RequestParam(name = "search", required = false) String content,
			@RequestParam(name = "category", required = false) String urlKey) {
		PageResponse<List<ProductDTO>> listData = null;
		if (content != null) {
			listData = productService.getProductContainName(content, pageable);
		} else if (urlKey != null) {
			listData = productService.getProductByUrlKeyCategory(urlKey, pageable);
		} else {
			listData = productService.getAllProduct(pageable);
		}

		return new ResponseData<>(HttpStatus.OK.value(), "Data products" ,listData);

	}

	@GetMapping("/{id}")
	public ResponseData<ProductDTO> getProductById(@PathVariable(name = "id") Long id) {
		ProductDTO data = productService.getProductById(id);
		return new ResponseData<>(HttpStatus.OK.value(), "Data product" ,data);
	}

	@PostMapping
	public ResponseData<Long> addProduct(@Valid @RequestPart("product") ProductRequest productRequest,
			@RequestPart("files") List<MultipartFile> files) {
		Long data = productService.addProduct(productRequest, files);
		return new ResponseData<>(HttpStatus.CREATED.value(), "Product added successfully",data);
	}

	@PostMapping("/upload")
	public ResponseData<List<String>> upload(@RequestPart("files") List<MultipartFile> files) {
		List<String> data = imageService.upload(files);

		return new ResponseData<>(HttpStatus.CREATED.value(), "Images upload successfully" ,data);
	}

	@PutMapping("/{idProduct}" )
	public ResponseData<Void> updateProduct(
			@PathVariable Long idProduct,
			@Valid @RequestPart("product") ProductRequest request,
	        @RequestPart(name = "files", required = false) List<MultipartFile> files) {
	    productService.updateProduct(idProduct, request, files);
	    return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Product updated successfully");

	}
	
	@DeleteMapping("/{id}")
	public ResponseData<Void> deleteProductById(@PathVariable (name = "id") Long id){
		productService.deleteProduct(id);
		return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User delete successfully");
	}
	
	@PatchMapping("/{idProduct}/size/{isSize}/quantity")
	public ResponseData<Void> updateProductQuantity(
			@PathVariable Long idProduct,
			@PathVariable Integer idSize,
			@RequestParam Integer quantitySubtract){
		productService.updateProductQuantityForSize(quantitySubtract, idProduct, idSize);
		return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Product quantity updated successfully");
		
	}
	

}
