package com.watermelon.service.imp;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.dto.request.ProductImageRequest;
import com.watermelon.dto.request.ProductRequest;
import com.watermelon.dto.request.ProductSizeRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ProductResponse;
import com.watermelon.exception.InvalidQuantityException;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.mapper.imp.ProductMapper;
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
import com.watermelon.service.CommonService;
import com.watermelon.service.ProductService;
import com.watermelon.utils.Constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductServiceImp implements ProductService {

	ProductRepository productRepository;
	SizeRepository sizeRepository;
	BrandRepository brandRepository;
	CategoryRepository categoryRepository;
	ProductQuantityRepository productQuantityRepository;
	CloudinaryServiceImp cloudinaryServiceImp;
	ImageRepository imageRepository;
	CommonService commonService;
	ProductMapper productMapper;
	

	@Transactional(readOnly = true)
	@Override
	public ProductResponse getProductById(Long id) {
		Product product = commonService.findProductById(id);
		return productMapper.toDTO(product);
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<List<ProductResponse>> getAllProduct(Pageable pageable) {
		Page<Product> pageProduct = productRepository.findByIsActiveTrue(pageable);
		List<ProductResponse> listProductDTO = productMapper.toDTO(pageProduct.getContent());

		return new PageResponse<>(
				pageProduct.getPageable().getPageNumber(),
				pageProduct.getSize(),
				pageProduct.getTotalPages(),
				pageProduct.getTotalElements(),
				listProductDTO);
	}

	
	@Transactional(readOnly = true)
	@Override
	public PageResponse<List<ProductResponse>> getProductContainName(String keyword, Pageable pageable) {
		Page<Product> pageProduct = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword, pageable);
		List<ProductResponse> listProductDTO = productMapper.toDTO(pageProduct.getContent());
		return new PageResponse<>(
				pageProduct.getPageable().getPageNumber(),
				pageProduct.getSize(),
				pageProduct.getTotalPages(),
				pageProduct.getTotalElements(),
				listProductDTO);
	}
 
	@Transactional
	@Override
	public boolean deleteProduct(Long id) {
		if (id != null && productRepository.existsById(id)) {
			productRepository.deleteById(id);
			log.info("delete product ID {} success", id);
		    return true;
		}
		log.error("delete product ID {} failed", id);
		return false;
	}

	
	 
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	@Override
	public boolean updateProduct(Long idProduct, ProductRequest request, List<MultipartFile> files) {
		boolean result = false;
		Product product = commonService.findProductById(idProduct);
		updateChangedFields(product, request);

		if (!CollectionUtils.isEmpty(files)) {
			List<Image> savedImages = helperSaveImage(files, product);
			product.getListImages().addAll(savedImages);
		}

		Product productUpdated = productRepository.save(product);
		if(!ObjectUtils.isEmpty(productUpdated)) {
			log.info("update product ID {} success", idProduct);
			result = true;
		}
		
		return result;
	}
	


	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	@Override
	public Long addProduct(ProductRequest productRequest, List<MultipartFile> files) {
		if (productRequest == null) {
			return null;
		}
		Brand brand = commonService.findBrandProductById(productRequest.brandId());
		Category category = commonService.findCategoryProductById(productRequest.categoryId());
		Product product = new Product();
		product.setName(productRequest.name());
		product.setShortDescription(productRequest.shortDescription());
		product.setDescription(productRequest.description());
		product.setSalePrice(productRequest.salePrice());
		product.setRegularPrice(productRequest.regularPrice());
		product.setBrand(brand);
		product.setCategory(category);
		product.setActive(true);

		Product productSaved = productRepository.save(product);
		// save product quantity
		List<ProductQuantity> savedQuantities = productRequest.listSizes().stream().map(sizeItem -> {
			ProductQuantity productQuantityWithSize = new ProductQuantity();
			Size size = commonService.findSizeProductById(sizeItem.id());
			productQuantityWithSize.setQuantity(sizeItem.quantity());
			productQuantityWithSize.setSize(size);
			productQuantityWithSize.setProduct(productSaved);
			return productQuantityWithSize;
		}).toList();

		productQuantityRepository.saveAll(savedQuantities);

		// save images
		List<Image> savedImages = helperSaveImage(files, productSaved);
		productSaved.setThumbnailImage(savedImages.get(0).getPath());
		productRepository.save(productSaved);

		productSaved.setListImages(savedImages.stream().collect(Collectors.toSet()));
		productSaved.setQuantityOfSizes(savedQuantities.stream().collect(Collectors.toSet()));
		log.info("add product ID {} success", productSaved.getId());
		return productSaved.getId();
	}
	


	@Override
	public PageResponse<List<ProductResponse>> getProductByUrlKeyCategory(String urlKey, Pageable pageable) {
		Page<Product> pageProduct = productRepository.findByCategory_UrlKeyAndIsActiveTrue(urlKey, pageable);
		if (pageProduct.isEmpty()) {
			throw new ResourceNotFoundException("URL_KEY_CATEGORY_NOT_FOUND", urlKey);
		}
		List<ProductResponse> listProductDTO = productMapper.toDTO(pageProduct.getContent());

		return new PageResponse<>(
				pageProduct.getPageable().getPageNumber(),
				pageProduct.getSize(), pageProduct.getTotalPages(),
				pageProduct.getTotalElements(),listProductDTO);
	}


	
	@Override
	public void updateProductQuantityForSize(int quantitySubtract, Long idProduct, Integer idSize) {
		Product product = commonService.findProductById(idProduct);
		Size size = commonService.findSizeProductById(idSize);

		ProductQuantity productQuantity = productQuantityRepository.findByProduct_IdAndSize_Id(idProduct, idSize);

		int quantityOld = productQuantity.getQuantity();

		if (quantitySubtract > quantityOld) {
			log.error("Quantity to subtract exceeds available quantity!");
			throw new InvalidQuantityException("Quantity to subtract exceeds available quantity!");
		}
		if (quantitySubtract > Constants.QUANTITY_PRODUCT_MAX_BUY) {
			log.error("Quantity to subtract must be less than or equal to "+ Constants.QUANTITY_PRODUCT_MAX_BUY);
			throw new InvalidQuantityException("Quantity to subtract must be less than or equal to "+ Constants.QUANTITY_PRODUCT_MAX_BUY);
		}
		productQuantity.setQuantity(quantityOld - quantitySubtract);
		productQuantity.setProduct(product);
		productQuantity.setSize(size);
		productQuantityRepository.save(productQuantity);
		log.info("Updated quantity of product ID {} with size ID {} successfully", idProduct, idSize);

	}
	
	@Override
	public void updateStatusProduct(Long idProduct, Boolean isActive) {
		Product product = commonService.findProductById(idProduct);
		product.setActive(isActive);
		productRepository.save(product);
		log.info("Updated status of product ID {}successfully", idProduct);
		
	}
	
	
	private List<Image> helperSaveImage(List<MultipartFile> files, Product product) {
		List<String> listPath = cloudinaryServiceImp.upload(files);
		List<Image> savedImages = listPath.stream().map(path -> {
			Image image = new Image();
			image.setPath(path);
			image.setProduct(product);
			return image;
		}).toList();

		imageRepository.saveAll(savedImages);
		return savedImages;
	}

	private void updateChangedFields(Product product, ProductRequest request) {
		if (product == null || request == null) {
			return;
		}
		if (!isEqual(product.getName(), request.name()))
			product.setName(request.name());

		if (!isEqual(product.getShortDescription(), request.shortDescription()))
			product.setShortDescription(request.shortDescription());

		if (!isEqual(product.getDescription(), request.description()))
			product.setDescription(request.description());

		if (!isEqual(product.getSalePrice(), request.salePrice()))
			product.setSalePrice (request.salePrice());

		if (!isEqual(product.getRegularPrice(), request.regularPrice()))
			product.setRegularPrice(request.regularPrice());

		if (request.brandId() != product.getBrand().getId()) {
			product.setBrand(brandRepository.findById(request.brandId()).orElse(null));
		}

		if (request.categoryId() != product.getCategory().getId()) {
			product.setCategory(categoryRepository.findById(request.categoryId()).orElse(null));
		}

		helperUpdateQuantityOfProductSize(product, request.listSizes());

		helperUpdateProductImages(product, request.listImages());
	}

	private void helperUpdateQuantityOfProductSize(Product product, List<ProductSizeRequest> newSizeList) {
		List<ProductQuantity> existingQuantities = productQuantityRepository.findByProduct_Id(product.getId());
		// list product quantity will be deleted
		Set<ProductQuantity> quantitiesToRemove = existingQuantities.stream().filter(
				quantity -> newSizeList.stream().noneMatch(s -> s.id() == quantity.getSize().getId()))
				.collect(Collectors.toSet());

		// delete quantity when no longer in list
		productQuantityRepository.deleteAll(quantitiesToRemove);

		// update or add quantity of size
		newSizeList.forEach(s -> {
			ProductQuantity existingQuantity = existingQuantities.stream()
					.filter(quantity -> quantity.getSize().getId() == s.id()).findFirst().orElse(null);

			if (existingQuantity != null) {
				// update
				existingQuantity.setQuantity(s.quantity());
			} else {
				// add
				ProductQuantity newQuantity = new ProductQuantity();
				Size size = sizeRepository.findById(s.id()).orElse(null);
				if (size != null) {
					newQuantity.setSize(size);
					newQuantity.setQuantity(s.quantity());
					newQuantity.setProduct(product);
					existingQuantities.add(productQuantityRepository.save(newQuantity));
				}
			}
		});
	}

	
	private void helperUpdateProductImages(Product product, List<ProductImageRequest> newImageList) {
		List<Image> existingImages = imageRepository.findByProduct_Id(product.getId());
		Set<Image> imagesToRemove = existingImages.stream()
				.filter(image -> newImageList.stream().noneMatch(i -> i.id() == image.getId()))
				.collect(Collectors.toSet());

		if (newImageList.size() == existingImages.size()) {
			return;
		}

		imageRepository.deleteAll(imagesToRemove);
	}

	// method supports null-safe comparison
	private boolean isEqual(Object obj1, Object obj2) {
		return (obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2));
	}

	
}
