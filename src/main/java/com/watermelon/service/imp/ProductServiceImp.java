package com.watermelon.service.imp;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.exception.NotFoundException;
import com.watermelon.model.entity.Brand;
import com.watermelon.model.entity.Category;
import com.watermelon.model.entity.Image;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.ProductQuantity;
import com.watermelon.model.entity.Size;
import com.watermelon.repository.BrandRepository;
import com.watermelon.repository.CategoryRepository;
import com.watermelon.repository.ImageRepository;
import com.watermelon.repository.ProductQuantityRepository;
import com.watermelon.repository.ProductRepository;
import com.watermelon.repository.SizeRepository;
import com.watermelon.service.ProductService;
import com.watermelon.service.dto.ImageDTO;
import com.watermelon.service.dto.ProductDTO;
import com.watermelon.service.dto.SizeDTO;
import com.watermelon.service.mapper.imp.ProductMapper;
import com.watermelon.utils.Constants;
import com.watermelon.viewandmodel.error.ResponsePageData;
import com.watermelon.viewandmodel.request.ProductRequest;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImp implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SizeRepository sizeRepository;
	@Autowired
	private BrandRepository brandRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductQuantityRepository productQuantityRepository;
	@Autowired
	private CloudinaryServiceImp cloudinaryServiceImp;
	@Autowired
	private ImageRepository imageRepository;

	@Transactional
	@Override
	public ProductDTO getProductById(Long id) {
		Optional<Product> product = Optional.ofNullable(
				productRepository.findById(id).orElseThrow(() -> new NotFoundException("product not found!")));
		ProductDTO result = new ProductMapper().toDTO(product.get());
		return result;
	}

	@Transactional
	@Override
	public ResponsePageData<List<ProductDTO>> getAllProduct(Pageable pageable) {
		Page<Product> pageProduct = productRepository.findAll(pageable);
		List<ProductDTO> listProductDTO = new ProductMapper().toDTO(pageProduct.getContent());

		ResponsePageData<List<ProductDTO>> result = new ResponsePageData<>(listProductDTO,
				pageProduct.getPageable().getPageNumber(), pageProduct.getSize(), pageProduct.getTotalPages(),
				pageProduct.getTotalElements());
		return result;
	}

	@Transactional
	@Override
	public ResponsePageData<List<ProductDTO>> getProductContainName(String keyword, Pageable pageable) {
		Page<Product> pageProduct = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
		List<ProductDTO> listProductDTO = new ProductMapper().toDTO(pageProduct.getContent());
		ResponsePageData<List<ProductDTO>> result = new ResponsePageData<>(listProductDTO,
				pageProduct.getPageable().getPageNumber(), pageProduct.getSize(), pageProduct.getTotalPages(),
				pageProduct.getTotalElements());
		return result;
	}

	@Transactional
	@Override
	public boolean deleteProduct(Long id) {
		return Optional.of(id).map(productId -> {
			productRepository.deleteById(id);
			return true;
		}).orElse(false);
	}

	@Transactional
	@Override
	public ProductDTO updateProduct(ProductDTO productDTO, List<MultipartFile> files) {
		Optional<Product> optionalProduct = productRepository.findById(productDTO.id());
		if (optionalProduct.isPresent()) {
			Product existingProduct = optionalProduct.get();

			// Copy các thuộc tính từ productDTO vào existingProduct
			// không cho cập nhật id
			BeanUtils.copyProperties(productDTO, existingProduct, "id");
			
			change(existingProduct,productDTO);

			if (files != null && !files.isEmpty()) {
				List<Image> savedImages = saveImage(files, existingProduct);
				existingProduct.getListImages().addAll(savedImages);
			}
			
			// Lưu lại product sau khi cập nhật
			productRepository.save(existingProduct);

			return new ProductMapper().toDTO(existingProduct);
		} else {
			throw new NotFoundException("Product not found!");
		}
	}
	public ProductDTO updateProduct(ProductDTO productDTO) {
		Optional<Product> optionalProduct = productRepository.findById(productDTO.id());
		if (optionalProduct.isPresent()) {
			Product existingProduct = optionalProduct.get();
			
			// Copy các thuộc tính từ productDTO vào existingProduct
			// không cho cập nhật id
			BeanUtils.copyProperties(productDTO, existingProduct, "id");
			
			change(existingProduct,productDTO);
			
			
			// Lưu lại product sau khi cập nhật
			productRepository.save(existingProduct);
			
			return new ProductMapper().toDTO(existingProduct);
		} else {
			throw new NotFoundException("Product not found!");
		}
	}

	@Transactional
	@Override
	public ProductDTO addProduct(ProductRequest productRequest, List<MultipartFile> files) {
		if (productRequest == null) {
			return null;
		}
		Brand brand = brandRepository.findById(productRequest.idBrand()).get();
		Category category = categoryRepository.findById(productRequest.idCategory()).get();

		Product product = new Product();
		product.setName(productRequest.name());
		product.setShortDescription(productRequest.shortDescription());
		product.setDescription(productRequest.description());
		product.setGtin(productRequest.gtin());
		product.setSku(productRequest.sku());
		product.setSlug(productRequest.slug());
		product.setPrice(productRequest.price());
		product.setTax(productRequest.tax());
		product.setBrand(brand);
		product.setCategory(category);

		Product mainProduct = productRepository.save(product);
		// Lưu ProductQuantity vào cơ sở dữ liệu
		List<ProductQuantity> savedQuantities = productRequest.listSize().stream().map(sizeItem -> {
			ProductQuantity productQuantityWithSize = new ProductQuantity();
			Size size = sizeRepository.findById(sizeItem.id()).get();
			productQuantityWithSize.setQuantity(sizeItem.quantity());
			productQuantityWithSize.setSize(size);
			productQuantityWithSize.setProduct(mainProduct);
			return productQuantityWithSize;
		}).collect(Collectors.toList());

		productQuantityRepository.saveAll(savedQuantities);

		List<Image> savedImages = saveImage(files, mainProduct);

		mainProduct.setListImages(savedImages.stream().collect(Collectors.toSet()));
		mainProduct.setQuantityOfSizes(savedQuantities.stream().collect(Collectors.toSet()));
		ProductDTO result = new ProductMapper().toDTO(mainProduct);

		return result;
	}

	
	private void change(Product product, ProductDTO productDTO) {
		if (product == null || productDTO == null) {
			return;
		}
		// Thực hiện so sánh và cập nhật các thuộc tính
		if (!isEqual(product.getName(), productDTO.name()))
			product.setName(productDTO.name());

		if (!isEqual(product.getShortDescription(), productDTO.shortDescription()))
			product.setShortDescription(productDTO.shortDescription());

		if (!isEqual(product.getDescription(), productDTO.description()))
			product.setDescription(productDTO.description());

		if (!isEqual(product.getGtin(), productDTO.gtin()))
			product.setGtin(productDTO.gtin());

		if (!isEqual(product.getSku(), productDTO.skug()))
			product.setSku(productDTO.skug());

		if (!isEqual(product.getSlug(), productDTO.slug()))
			product.setSlug(productDTO.slug());

		if (!isEqual(product.getPrice(), productDTO.price()))
			product.setPrice(productDTO.price());

		if (!isEqual(product.getTax(), productDTO.tax()))
			product.setTax(productDTO.tax());

		// Nếu cần cập nhật cả brand hoặc category, bạn có thể sử dụng id của chúng
		if (productDTO.brand() != null && productDTO.brand().id() != product.getBrand().getId()) {
			// Cập nhật brand
			product.setBrand(brandRepository.findById(productDTO.brand().id()).orElse(null));
		}

		if (productDTO.category() != null && productDTO.category().id() != product.getCategory().getId()) {
			// Cập nhật category
			product.setCategory(categoryRepository.findById(productDTO.category().id()).orElse(null));
		}

		// Cập nhật ProductQuantities
		updateProductQuantities(product, productDTO.listSize());

		// Cập nhật Images
		updateProductImages(product, productDTO.listImages());
	}

	// Phương thức cập nhật ProductQuantities
	private void updateProductQuantities(Product product, List<SizeDTO> newSizeList) {
		List<ProductQuantity> existingQuantities = productQuantityRepository.findByProduct_Id(product.getId());
		// Xác định các ProductQuantities cần xóa
		Set<ProductQuantity> quantitiesToRemove = existingQuantities.stream().filter(
				quantity -> newSizeList.stream().noneMatch(sizeDTO -> sizeDTO.id() == quantity.getSize().getId()))
				.collect(Collectors.toSet());

		// Xóa các ProductQuantities không còn trong danh sách mới
		productQuantityRepository.deleteAll(quantitiesToRemove);

		// Cập nhật hoặc thêm mới ProductQuantities
		newSizeList.forEach(sizeDTO -> {
			ProductQuantity existingQuantity = existingQuantities.stream()
					.filter(quantity -> quantity.getSize().getId() == sizeDTO.id()).findFirst().orElse(null);

			if (existingQuantity != null) {
				// Nếu ProductQuantity đã tồn tại, cập nhật
				existingQuantity.setQuantity(sizeDTO.quantity());
			} else {
				// Nếu ProductQuantity chưa tồn tại, thêm mới
				ProductQuantity newQuantity = new ProductQuantity();
				Size size = sizeRepository.findById(sizeDTO.id()).orElse(null);
				if (size != null) {
					newQuantity.setSize(size);
					newQuantity.setQuantity(sizeDTO.quantity());
					newQuantity.setProduct(product);
					existingQuantities.add(productQuantityRepository.save(newQuantity));
				}
			}
		});
	}

	// Phương thức cập nhật Images
	private void updateProductImages(Product product, List<ImageDTO> newImageList) {
		List<Image> existingImages = imageRepository.findByProduct_Id(product.getId());
		// Xác định các Images cần xóa
		Set<Image> imagesToRemove = existingImages.stream()
				.filter(image -> newImageList.stream().noneMatch(imageDTO -> imageDTO.id() == image.getId()))
				.collect(Collectors.toSet());

		if(newImageList.size() == existingImages.size()) {
			// phuong thuc phuc vu cho muc dich xoa hinh anh
			// neu so luong hinh anh nhu cu thi khong lam gi ca
			return;
		}

		// Xóa các Images không còn trong danh sách mới
		imageRepository.deleteAll(imagesToRemove);

//		// Cập nhật hoặc thêm mới Images
//		newImageList.forEach(imageDTO -> {
//			Image existingImage = existingImages.stream().filter(image -> image.getId() == imageDTO.id()).findFirst()
//					.orElse(null);
//
//			if (existingImage != null) {
//				// Nếu Image đã tồn tại, có thể cập nhật thông tin khác nếu cần
//				// Hiện tại không có thông tin cần cập nhật
//			} else {
//				// Nếu Image chưa tồn tại, thêm mới
//				Image newImage = new Image();
//				newImage.setPath(imageDTO.path());
//				newImage.setProduct(product);
//				existingImages.add(imageRepository.save(newImage));
//			}
//		});
	}

	// Phương thức hỗ trợ so sánh null-safe
	private boolean isEqual(Object obj1, Object obj2) {
		return (obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2));
	}

	@Override
	public ResponsePageData<List<ProductDTO>> getProductByUrlKeyCategory(String urlKey, Pageable pageable) {
		Page<Product> pageProduct = productRepository.findByCategory_UrlKey(urlKey, pageable);
		if (pageProduct.isEmpty()) {
	        throw new NotFoundException("Not found by url key category: " + urlKey);
	    }
		List<ProductDTO> listProductDTO = new ProductMapper().toDTO(pageProduct.getContent());

		ResponsePageData<List<ProductDTO>> result = new ResponsePageData<>(listProductDTO,
				pageProduct.getPageable().getPageNumber(), pageProduct.getSize(), pageProduct.getTotalPages(),
				pageProduct.getTotalElements());
		return result;
	}

	
	private List<Image> saveImage(List<MultipartFile> files, Product product) {
		List<String> listPath = cloudinaryServiceImp.upload(files);
		// Lưu Image vào cơ sở dữ liệu
		List<Image> savedImages = listPath.stream().map(path -> {
			Image image = new Image();
			image.setPath(path);
			image.setProduct(product);
			return image;
		}).collect(Collectors.toList());

		imageRepository.saveAll(savedImages);
		return savedImages;
	}
	@Override
	public void updateQuantityProduct(int quantitySubtract, Long idProduct, Integer idSize) {
		Product product = productRepository.findById(idProduct)
				.orElseThrow(() -> new RuntimeException("Product not found!"));
		Size size = sizeRepository.findById(idSize)
				.orElseThrow(() -> new RuntimeException("Size not found!"));
		
		ProductQuantity productQuantity = productQuantityRepository.findByProduct_IdAndSize_Id(idProduct, idSize);
		
		int quantityOld = productQuantity.getQuantity();
		
		if(quantitySubtract>quantityOld) {
			throw new RuntimeException("Quantity not enough!");
		}
		if(quantitySubtract> Constants.QUANTITY_PRODUCT_MAX_BUY) {
			throw new RuntimeException("Quantity must to less than or equal "+Constants.QUANTITY_PRODUCT_MAX_BUY);
		}
		productQuantity.setQuantity(quantityOld-quantitySubtract);
		productQuantity.setProduct(product);
		productQuantity.setSize(size);
		
	}
}
