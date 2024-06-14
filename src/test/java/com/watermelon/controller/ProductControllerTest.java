package com.watermelon.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.dto.BrandDTO;
import com.watermelon.dto.CategoryDTO;
import com.watermelon.dto.ImageDTO;
import com.watermelon.dto.ProductDTO;
import com.watermelon.dto.SizeDTO;
import com.watermelon.dto.request.ProductRequest;
import com.watermelon.dto.request.ProductSizeRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.service.ImageService;
import com.watermelon.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
@WithMockUser(username = "admin", roles = {"ADMIN","USER"})
class ProductControllerTest {

	@MockBean
	private ProductService productService;
	
	@MockBean
	private ImageService imageService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductController productController;

	private ProductDTO productDTO;
	private CategoryDTO categoryDTO;
	private BrandDTO brandDTO;
	private ProductRequest productRequest;
	private List<ProductSizeRequest> listProductSizeRequests;
	private List<ProductDTO> listProductDTOs;
	private List<ImageDTO> listImageDTOs;
	private List<SizeDTO> listSizes;
	private PageResponse<List<ProductDTO>> pageResponse;

	@BeforeEach
	void initData() {

		listImageDTOs = new ArrayList<>();
		listImageDTOs.add(ImageDTO.builder().id(1).path("http://image1.png").build());
		listImageDTOs.add(ImageDTO.builder().id(2).path("http://image2.png").build());
		listImageDTOs.add(ImageDTO.builder().id(3).path("http://image3.png").build());

		listSizes = new ArrayList<>();
		listSizes.add(SizeDTO.builder().id(1).name("M").quantity(10).build());
		listSizes.add(SizeDTO.builder().id(2).name("S").quantity(17).build());
		listSizes.add(SizeDTO.builder().id(3).name("L").quantity(15).build());
		categoryDTO = CategoryDTO.builder().id(1).name("Nam").build();
		brandDTO = BrandDTO.builder().id(1).name("Nike").build();
		productDTO = ProductDTO.builder()
				.id(1L)
				.name("Áo thun thể thao chính hãng")
				.shortDescription("Áo thể thao chất liệu vãi mềm mỏng co dãn tốt")
				.description("Đây là sản phẩm chính hãng của thương hiệu Nike")
				.price(new BigDecimal("250000"))
				.brand(brandDTO)
				.category(categoryDTO)
				.listImages(listImageDTOs)
				.listSize(listSizes).build();

		listProductDTOs = List.of(
				ProductDTO.builder()
						.id(1L)
						.name("Áo thun thể thao chính hãng")
						.shortDescription("Áo thể thao chất liệu vãi mềm mỏng co dãn tốt")
						.description("Đây là sản phẩm chính hãng của thương hiệu Nike")
						.price(new BigDecimal("250000"))
						.brand(brandDTO)
						.category(categoryDTO)
						.listImages(listImageDTOs)
						.listSize(listSizes).build(),
				ProductDTO.builder()
						.id(2L)
						.name("Áo thun thể thao chính hãng")
						.shortDescription("Áo thể thao chất liệu vãi mềm mỏng co dãn tốt")
						.description("Đây là sản phẩm chính hãng của thương hiệu Nike")
						.price(new BigDecimal("250000"))
						.brand(brandDTO)
						.category(categoryDTO)
						.listImages(listImageDTOs)
						.listSize(listSizes)
						.build());
		pageResponse = new PageResponse<List<ProductDTO>>
		(0, 20, 1, 2, listProductDTOs);

		listProductSizeRequests = List.of(
				ProductSizeRequest.builder().id(1).quantity(10).build(),
				ProductSizeRequest.builder().id(2).quantity(17).build(),
				ProductSizeRequest.builder().id(3).quantity(15).build());
				
		
		productRequest = ProductRequest.builder()
				.name("Áo thể thao nam")
				.shortDescription("Áo thể thao chất liệu vãi mềm mỏng co dãn tốt")
				.description("Đây là sản phẩm chính hãng của thương hiệu Nike")
				.price(new BigDecimal("250000"))
				.tax(0.2)
				.idBrand(1)
				.idCategory(1)
				.listSize(listProductSizeRequests)
				.build();

	}

	@Test
	void getProductById_ValidRequest_Success() throws Exception {
		
		when(productService.getProductById(1L)).thenReturn(productDTO);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/products/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("status").value(200))
				.andExpect(jsonPath("message").value("Data product"))
				.andExpect(jsonPath("data.id").value(1L))
				.andReturn()
				;
	}

	@Test
	void getProductById_IdInvalid_Fail() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/products/111AS").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
				.andExpect(MockMvcResultMatchers.jsonPath("message").value("PathVariable invalid")).andReturn();
	}
	
	@Test
	void getProducts_ValidRequest_Success() throws JsonProcessingException, Exception {
		Pageable pageable = PageRequest
				.of(0, 20, Sort.by(Sort.Direction.DESC, "price"));
	    PageResponse<List<ProductDTO>> pageResponse = new PageResponse<>(
	    		0,20,1,2L,listProductDTOs
	    );
	    when(productService.getAllProduct(pageable)).thenReturn(pageResponse);
	    mockMvc.perform(get("/api/products")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	            .andExpect(jsonPath("$.status").value(200))
	            .andExpect(jsonPath("$.message").value("Data products"))
	            .andExpect(jsonPath("$.data.currentPage").value(0))       
	            .andExpect(jsonPath("$.data.size").value(20))             
	            .andExpect(jsonPath("$.data.totalPage").value(1))         
	            .andExpect(jsonPath("$.data.totalElement").value(2))       
	            .andReturn();
	}
	@Test
	void getProducts_SearchParam_Success() throws  Exception {
		Pageable pageable = PageRequest
				.of(0, 20, Sort.by(Sort.Direction.DESC, "price"));
		String content = "Áo thun";
		PageResponse<List<ProductDTO>> pageResponse = new PageResponse<>(
				0,20,1,2L,listProductDTOs 
		);
		when(productService.getProductContainName(content,pageable)).thenReturn(pageResponse);
		mockMvc.perform(get("/api/products")
				.param("search", content)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(200))
				.andExpect(jsonPath("$.message").value("Data products"))
				.andExpect(jsonPath("$.data.currentPage").value(0))      
				.andExpect(jsonPath("$.data.size").value(20))              
				.andExpect(jsonPath("$.data.totalPage").value(1))       
				.andExpect(jsonPath("$.data.totalElement").value(2))   
				.andReturn();
	}
	@Test
	void getProducts_UrlKeyCategoryParam_Success() throws  Exception {
		Pageable pageable = PageRequest
				.of(0, 20, Sort.by(Sort.Direction.DESC, "price"));
		String urlKey = "nam";
		PageResponse<List<ProductDTO>> pageResponse = new PageResponse<>(
				0,20,1,2L,listProductDTOs 
				);
		when(productService.getProductByUrlKeyCategory(urlKey,pageable)).thenReturn(pageResponse);
		mockMvc.perform(get("/api/products")
				.param("category", urlKey)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(200))
				.andExpect(jsonPath("$.message").value("Data products"))
				.andExpect(jsonPath("$.data.currentPage").value(0))      
				.andExpect(jsonPath("$.data.size").value(20))              
				.andExpect(jsonPath("$.data.totalPage").value(1))       
				.andExpect(jsonPath("$.data.totalElement").value(2))   
				.andReturn();
	}
	@Test
	void deleteProductById_ValidRequest_Success() throws Exception {
		
		when(productService.deleteProduct(anyLong())).thenReturn(true);
		
		mockMvc.perform(delete("/api/products/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.status").value(204))
				.andExpect(jsonPath("$.message").value("User delete successfully"))
				.andReturn()
				;
	}
	
	@Test
	void addProduct_ValidRequest_Success() throws Exception {
	    MockMultipartFile file1 = new MockMultipartFile("files", "file1.jpg", "image/jpeg", "file1contents".getBytes());
	    MockMultipartFile file2 = new MockMultipartFile("files", "file2.jpg", "image/jpeg", "file2contents".getBytes());
	    MockMultipartFile productRequestJson = 
	    		new MockMultipartFile(
	    				"product", 
	    				null,
	    				MediaType.APPLICATION_JSON_VALUE,
	    				objectMapper.writeValueAsBytes(productRequest));
	    
	    when(productService.addProduct(productRequest, List.of(file1,file2))).thenReturn(123L);

	    mockMvc.perform(multipart("/api/products")
	            .file(file1)
	            .file(file2)
	            .file(productRequestJson)
	            .with(request -> {
	            	request.setMethod("POST");
	            	return request;
	            })
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().is(200))
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	            .andExpect(jsonPath("$.status").value(201))
	            .andExpect(jsonPath("$.message").value("Product added successfully"))
	            .andExpect(jsonPath("$.data").value(123L))
	            .andReturn();
	}
	
	@Test
	void updateProduct_ValidRequest_Success() throws Exception {
		MockMultipartFile productRequestJson = 
				new MockMultipartFile(
						"product", 
						null,
						MediaType.APPLICATION_JSON_VALUE,
						objectMapper.writeValueAsBytes(productRequest));
		when(productService.updateProduct(anyLong(),any(ProductRequest.class),anyList()))
		.thenReturn(true);
		mockMvc.perform(multipart("/api/products/1")
				.file(productRequestJson)
				.with(request -> {
					request.setMethod("PUT");
					return request;
				})
				.contentType(MediaType.MULTIPART_FORM_DATA))
		.andExpect(status().is(200))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.status").value(202))
		.andExpect(jsonPath("$.message").value("Product updated successfully"))
		.andReturn();
	}

	@Test
	void upload_ValidRequest_Success() throws Exception {
	    MockMultipartFile file1 = new MockMultipartFile("files", "file1.jpg", "image/jpeg", "file1contents".getBytes());
	    MockMultipartFile file2 = new MockMultipartFile("files", "file2.jpg", "image/jpeg", "file2contents".getBytes());

	    List<String> uploadedImages = List.of("file1.jpg", "file2.jpg");
	    when(imageService.upload(anyList())).thenReturn(uploadedImages);

	    mockMvc.perform(multipart("/api/products/upload")
	            .file(file1)
	            .file(file2))
	            .andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	            .andExpect(jsonPath("$.status").value(201))
	            .andExpect(jsonPath("$.message").value("Images upload successfully"))
	            .andExpect(jsonPath("$.data").isArray())
	            .andExpect(jsonPath("$.data.length()").value(2))
	            .andExpect(jsonPath("$.data[0]").value("file1.jpg"))
	            .andExpect(jsonPath("$.data[1]").value("file2.jpg"));
	}


}
