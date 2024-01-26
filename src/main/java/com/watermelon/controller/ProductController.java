package com.watermelon.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.service.ImageService;
import com.watermelon.service.ProductService;
import com.watermelon.service.dto.ProductDTO;
import com.watermelon.viewandmodel.error.ResponseData;
import com.watermelon.viewandmodel.error.ResponsePageData;
import com.watermelon.viewandmodel.request.ProductRequest;

@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ImageService imageService;

	@GetMapping("/products")
	public ResponseEntity<ResponseData> getAllProduct(
			@PageableDefault(page = 0, size = 20) @SortDefaults(@SortDefault(direction = Sort.Direction.DESC, sort = {
					"price" })) Pageable pageable) {
		Optional<ResponsePageData<List<ProductDTO>>> listData = Optional
				.ofNullable(productService.getAllProduct(pageable));
		return ResponseEntity.ok()
				.body(new ResponseData(listData, HttpStatus.OK.name(), HttpStatus.OK.getReasonPhrase()));
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<ResponseData> getProductById(@PathVariable(name = "id") Long id) {
		ProductDTO data = productService.getProductById(id);
		return ResponseEntity.ok().body(new ResponseData(data, HttpStatus.OK.name(), HttpStatus.OK.getReasonPhrase()));
	}

	@PostMapping("/products")
	public ResponseEntity<ResponseData> addProduct(@RequestPart("product") ProductRequest productRequest,
			@RequestPart("file") List<MultipartFile> files) {
		System.out.println("Hello");
		ProductDTO data = productService.addProduct(productRequest, files);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseData(data, HttpStatus.CREATED.name(), HttpStatus.CREATED.getReasonPhrase()));
	}

	@PostMapping("/products/upload")
	public ResponseEntity<ResponseData> upload(@RequestPart("files") List<MultipartFile> files) {
		List<String> data = imageService.upload(files);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseData(data, HttpStatus.CREATED.name(), HttpStatus.CREATED.getReasonPhrase()));
	}


}
