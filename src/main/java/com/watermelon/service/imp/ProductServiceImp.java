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

import com.watermelon.dto.ProductDTO;
import com.watermelon.dto.request.ProductImageRequest;
import com.watermelon.dto.request.ProductRequest;
import com.watermelon.dto.request.ProductSizeRequest;
import com.watermelon.dto.response.PageResponse;
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

	
	/**
     * Retrieves a product by its ID.
     * 
     * @param id The ID of the product to retrieve.
     * @return The ProductDTO representing the retrieved product.
     * @throws ResourceNotFoundException if the product with the specified ID is not found.
     */
	@Transactional(readOnly = true)
	@Override
	public ProductDTO getProductById(Long id) {
		Product product = commonService.findProductById(id);
		return ProductMapper.getInstance().toDTO(product);
	}

	 /**
     * Retrieves all products with pagination.
     * 
     * @param pageable The pagination information.
     * @return A ResponsePageData containing the list of ProductDTOs and pagination details.
     */
	@Transactional(readOnly = true)
	@Override
	public PageResponse<List<ProductDTO>> getAllProduct(Pageable pageable) {
		Page<Product> pageProduct = productRepository.findByIsActiveTrue(pageable);
		List<ProductDTO> listProductDTO = ProductMapper.getInstance().toDTO(pageProduct.getContent());

		return new PageResponse<>(
				pageProduct.getPageable().getPageNumber(),
				pageProduct.getSize(),
				pageProduct.getTotalPages(),
				pageProduct.getTotalElements(),
				listProductDTO);
	}

	 /**
     * Retrieves products containing a specified keyword with pagination.
     * 
     * @param keyword The keyword to search for in product names.
     * @param pageable The pagination information.
     * @return A ResponsePageData containing the list of ProductDTOs matching the keyword and pagination details.
     */
	@Transactional(readOnly = true)
	@Override
	public PageResponse<List<ProductDTO>> getProductContainName(String keyword, Pageable pageable) {
		Page<Product> pageProduct = productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword, pageable);
		List<ProductDTO> listProductDTO = ProductMapper.getInstance().toDTO(pageProduct.getContent());
		return new PageResponse<>(
				pageProduct.getPageable().getPageNumber(),
				pageProduct.getSize(),
				pageProduct.getTotalPages(),
				pageProduct.getTotalElements(),
				listProductDTO);
	}

	 /**
     * Deletes a product by its ID.
     * 
     * @param id The ID of the product to delete.
     * @return true if the product is deleted successfully, false otherwise.
     */
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

	
	 /**
     * Updates a product with new details and optional images.
     * 
     * @param request The updated product details.
     * @param files The list of image files to associate with the product.
     * @return The ProductDTO representing the updated product.
     * @throws ResourceNotFoundException if the product to update is not found.
     */
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
	

	 /**
     * Adds a new product with specified details and associated images.
     * 
     * @param productRequest The request containing new product details.
     * @param files The list of image files to associate with the product.
     * @return The ProductDTO representing the added product.
     */
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	@Override
	public Long addProduct(ProductRequest productRequest, List<MultipartFile> files) {
		if (productRequest == null) {
			return null;
		}
		Brand brand = commonService.findBrandProductById(productRequest.idBrand());
		Category category = commonService.findCategoryProductById(productRequest.idCategory());
		Product product = new Product();
		product.setName(productRequest.name());
		product.setShortDescription(productRequest.shortDescription());
		product.setDescription(productRequest.description());
		product.setPrice(productRequest.price());
		product.setTax(productRequest.tax());
		product.setBrand(brand);
		product.setCategory(category);
		product.setActive(true);

		Product productSaved = productRepository.save(product);
		// save product quantity
		List<ProductQuantity> savedQuantities = productRequest.listSize().stream().map(sizeItem -> {
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

		productSaved.setListImages(savedImages.stream().collect(Collectors.toSet()));
		productSaved.setQuantityOfSizes(savedQuantities.stream().collect(Collectors.toSet()));
		log.info("add product ID {} success", productSaved.getId());
		return productSaved.getId();
	}
	

	/**
	 * Retrieves a list of products based on the URL key of a category with pagination.
	 *
	 * @param urlKey   The URL key of the category to filter products by.
	 * @param pageable The pagination information.
	 * @return A ResponsePageData containing the list of ProductDTOs matching the category URL key and pagination details.
	 * @throws ResourceNotFoundException if no products are found with the specified category URL key.
	 */
	@Override
	public PageResponse<List<ProductDTO>> getProductByUrlKeyCategory(String urlKey, Pageable pageable) {
		Page<Product> pageProduct = productRepository.findByCategory_UrlKeyAndIsActiveTrue(urlKey, pageable);
		if (pageProduct.isEmpty()) {
			throw new ResourceNotFoundException("URL_KEY_CATEGORY_NOT_FOUND", urlKey);
		}
		List<ProductDTO> listProductDTO = ProductMapper.getInstance().toDTO(pageProduct.getContent());

		return new PageResponse<>(
				pageProduct.getPageable().getPageNumber(),
				pageProduct.getSize(), pageProduct.getTotalPages(),
				pageProduct.getTotalElements(),listProductDTO);
	}


	/**
	 * Updates the quantity of a product for a specific size by subtracting a specified quantity.
	 *
	 * @param quantitySubtract The quantity to subtract from the product's size.
	 * @param idProduct        The ID of the product.
	 * @param idSize           The ID of the size to update quantity for.
	 * @throws ResourceNotFoundException      if the product or size is not found.
	 * @throws RuntimeException       if the specified quantity to subtract is greater than available quantity or exceeds the maximum allowed.
	 */
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
	
	/**
	 * Saves uploaded images for a product during update.
	 *
	 * @param files   The list of image files to upload.
	 * @param product The product entity to associate the uploaded images with.
	 * @return The list of Image entities representing the saved images.
	 */
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

	/**
	 * Updates the specified fields of a product based on changes in the corresponding ProductDTO.
	 * This method is invoked during product update.
	 *
	 * @param product    The product entity to update.
	 * @param request The ProductDTO containing updated field values.
	 */
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

		if (!isEqual(product.getPrice(), request.price()))
			product.setPrice(request.price());

		if (!isEqual(product.getTax(), request.tax()))
			product.setTax(request.tax());

		if (request.idBrand() != product.getBrand().getId()) {
			product.setBrand(brandRepository.findById(request.idBrand()).orElse(null));
		}

		if (request.idCategory() != product.getCategory().getId()) {
			product.setCategory(categoryRepository.findById(request.idCategory()).orElse(null));
		}

		helperUpdateQuantityOfProductSize(product, request.listSize());

		helperUpdateProductImages(product, request.listImages());
	}

	/**
	 * Updates the product quantity based on the provided list of size changes.
	 * This method is invoked during product update.
	 *
	 * @param product    The product entity to update quantity for.
	 * @param newSizeList The list of SizeDTO objects representing the updated sizes.
	 */
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

	/**
	 * Updates the product images based on the provided list of updated images.
	 * This method is invoked during product update.
	 *
	 * @param product     The product entity to update images for.
	 * @param newImageList The list of ImageDTO objects representing the updated images.
	 */
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
