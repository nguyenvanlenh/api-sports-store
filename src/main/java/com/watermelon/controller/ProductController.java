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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.dto.request.ProductRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ProductResponse;
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
	public ResponseData<PageResponse<List<ProductResponse>>> getProducts(
			@PageableDefault(page = 0, size = 20) 
			@SortDefaults(
					@SortDefault(direction = Sort.Direction.DESC, sort = {"price" })) Pageable pageable) {
		PageResponse<List<ProductResponse>> listData = productService.getAllProduct(pageable);
		return ResponseData.<PageResponse<List<ProductResponse>>>builder()
				.status(HttpStatus.OK.value())
				.message("Products data")
				.data(listData)
				.build();

	}

	@GetMapping("/{id}")
	public ResponseData<ProductResponse> getProductById(@PathVariable(name = "id") Long id) {
		ProductResponse data = productService.getProductById(id);
		return ResponseData.<ProductResponse>builder()
				.status(HttpStatus.OK.value())
				.message("Product data")
				.data(data)
				.build();
	}

	@PostMapping
	public ResponseData<Long> addProduct(@Valid @RequestPart("product") ProductRequest productRequest,
			@RequestPart("files") List<MultipartFile> files) {
		Long data = productService.addProduct(productRequest, files);
		return ResponseData.<Long>builder()
				.status(HttpStatus.CREATED.value())
				.message("Product added successfully")
				.data(data)
				.build();
	}

	@PostMapping("/upload")
	public ResponseData<List<String>> upload(@RequestPart("files") List<MultipartFile> files) {
		List<String> data = imageService.upload(files);
		return ResponseData.<List<String>>builder()
				.status(HttpStatus.CREATED.value())
				.message("Images upload successfully")
				.data(data)
				.build();
	}

	@PutMapping("/{productId}" )
	public ResponseData<Void> updateProduct(
			@PathVariable Long productId,
			@Valid @RequestPart("product") ProductRequest request,
	        @RequestPart(name = "files", required = false) List<MultipartFile> files) {
	    productService.updateProduct(productId, request, files);
	    return ResponseData.<Void>builder()
				.status(HttpStatus.ACCEPTED.value())
				.message("Product updated successfully")
				.build();

	}
	
	@DeleteMapping("/{productId}")
	public ResponseData<Void> deleteProductById(@PathVariable (name = "productId") Long id){
		productService.deleteProduct(id);
		return ResponseData.<Void>builder()
				.status(HttpStatus.NO_CONTENT.value())
				.message("User delete successfully")
				.build();
	}
	
	@PatchMapping("/{productId}/size/{isSize}/quantity")
	public ResponseData<Void> updateProductQuantity(
			@PathVariable Long productId,
			@PathVariable Integer idSize,
			@RequestParam Integer quantitySubtract){
		productService.updateProductQuantityForSize(quantitySubtract, productId, idSize);
		return ResponseData.<Void>builder()
				.status(HttpStatus.ACCEPTED.value())
				.message("Product quantity updated successfully")
				.build();
		
	}
	
	@PatchMapping("/{productId}")
	public ResponseData<Void> updateProductStatus(@PathVariable Long productId, @RequestBody Boolean status){
		productService.updateProductStatus(productId, status);
		return ResponseData.<Void>builder()
				.status(HttpStatus.ACCEPTED.value())
				.message("Product quantity updated successfully")
				.build();
	}
	

}
