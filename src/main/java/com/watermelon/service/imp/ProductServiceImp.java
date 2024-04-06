package com.watermelon.service.imp;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.watermelon.dto.ImageDTO;
import com.watermelon.dto.ProductDTO;
import com.watermelon.dto.SizeDTO;
import com.watermelon.dto.mapper.imp.ProductMapper;
import com.watermelon.dto.request.ProductRequest;
import com.watermelon.dto.response.ResponsePageData;
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
import com.watermelon.utils.Constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImp implements ProductService {

	ProductRepository productRepository;

	SizeRepository sizeRepository;
	BrandRepository brandRepository;
	CategoryRepository categoryRepository;
	ProductQuantityRepository productQuantityRepository;
	CloudinaryServiceImp cloudinaryServiceImp;
	ImageRepository imageRepository;

	@Transactional(readOnly = true)
	@Override
	public ProductDTO getProductById(Long id) {
		Product product = 
				productRepository.findById(id).orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", id));
		ProductDTO result = new ProductMapper().toDTO(product);
		return result;
	}

	@Transactional(readOnly = true)
	@Override
	public ResponsePageData<List<ProductDTO>> getAllProduct(Pageable pageable) {
		Page<Product> pageProduct = productRepository.findAll(pageable);
		List<ProductDTO> listProductDTO = new ProductMapper().toDTO(pageProduct.getContent());

		ResponsePageData<List<ProductDTO>> result = new ResponsePageData<>(listProductDTO,
				pageProduct.getPageable().getPageNumber(), pageProduct.getSize(), pageProduct.getTotalPages(),
				pageProduct.getTotalElements());
		return result;
	}

	@Transactional(readOnly = true)
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
			BeanUtils.copyProperties(productDTO, existingProduct, "id");

			updateChangedFields(existingProduct, productDTO);

			if (files != null && !files.isEmpty()) {
				List<Image> savedImages = saveImage(files, existingProduct);
				existingProduct.getListImages().addAll(savedImages);
			}

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

			BeanUtils.copyProperties(productDTO, existingProduct, "id");

			updateChangedFields(existingProduct, productDTO);

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
		product.setPrice(productRequest.price());
		product.setTax(productRequest.tax());
		product.setBrand(brand);
		product.setCategory(category);

		Product mainProduct = productRepository.save(product);
		// save product quantity
		List<ProductQuantity> savedQuantities = productRequest.listSize().stream().map(sizeItem -> {
			ProductQuantity productQuantityWithSize = new ProductQuantity();
			Size size = sizeRepository.findById(sizeItem.id()).get();
			productQuantityWithSize.setQuantity(sizeItem.quantity());
			productQuantityWithSize.setSize(size);
			productQuantityWithSize.setProduct(mainProduct);
			return productQuantityWithSize;
		}).collect(Collectors.toList());

		productQuantityRepository.saveAll(savedQuantities);

		// save images
		List<Image> savedImages = saveImage(files, mainProduct);

		mainProduct.setListImages(savedImages.stream().collect(Collectors.toSet()));
		mainProduct.setQuantityOfSizes(savedQuantities.stream().collect(Collectors.toSet()));
		ProductDTO result = new ProductMapper().toDTO(mainProduct);

		return result;
	}

	// method only updated when the field on the product is changed
	private void updateChangedFields(Product product, ProductDTO productDTO) {
		if (product == null || productDTO == null) {
			return;
		}
		if (!isEqual(product.getName(), productDTO.name()))
			product.setName(productDTO.name());

		if (!isEqual(product.getShortDescription(), productDTO.shortDescription()))
			product.setShortDescription(productDTO.shortDescription());

		if (!isEqual(product.getDescription(), productDTO.description()))
			product.setDescription(productDTO.description());

		if (!isEqual(product.getPrice(), productDTO.price()))
			product.setPrice(productDTO.price());

		if (!isEqual(product.getTax(), productDTO.tax()))
			product.setTax(productDTO.tax());

		if (productDTO.brand() != null && productDTO.brand().id() != product.getBrand().getId()) {
			product.setBrand(brandRepository.findById(productDTO.brand().id()).orElse(null));
		}

		if (productDTO.category() != null && productDTO.category().id() != product.getCategory().getId()) {
			product.setCategory(categoryRepository.findById(productDTO.category().id()).orElse(null));
		}

		updateQuantityOfProductSize(product, productDTO.listSize());

		updateProductImages(product, productDTO.listImages());
	}

	// method update product quantity when update product
	private void updateQuantityOfProductSize(Product product, List<SizeDTO> newSizeList) {
		List<ProductQuantity> existingQuantities = productQuantityRepository.findByProduct_Id(product.getId());
		// list product quantity will be deleted
		Set<ProductQuantity> quantitiesToRemove = existingQuantities.stream().filter(
				quantity -> newSizeList.stream().noneMatch(sizeDTO -> sizeDTO.id() == quantity.getSize().getId()))
				.collect(Collectors.toSet());

		// delete quantity when no longer in list
		productQuantityRepository.deleteAll(quantitiesToRemove);

		// update or add quantity of size
		newSizeList.forEach(sizeDTO -> {
			ProductQuantity existingQuantity = existingQuantities.stream()
					.filter(quantity -> quantity.getSize().getId() == sizeDTO.id()).findFirst().orElse(null);

			if (existingQuantity != null) {
				// update
				existingQuantity.setQuantity(sizeDTO.quantity());
			} else {
				// add
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

	// method update images when update product
	private void updateProductImages(Product product, List<ImageDTO> newImageList) {
		List<Image> existingImages = imageRepository.findByProduct_Id(product.getId());
		// Xác định các Images cần xóa
		Set<Image> imagesToRemove = existingImages.stream()
				.filter(image -> newImageList.stream().noneMatch(imageDTO -> imageDTO.id() == image.getId()))
				.collect(Collectors.toSet());

		if (newImageList.size() == existingImages.size()) {
			// phuong thuc phuc vu cho muc dich xoa hinh anh
			// neu so luong hinh anh nhu cu thi khong lam gi ca
			return;
		}

		// Xóa các Images không còn trong danh sách mới
		imageRepository.deleteAll(imagesToRemove);
	}

	// method supports null-safe comparison
	private boolean isEqual(Object obj1, Object obj2) {
		return (obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2));
	}

	// method get list product by url key category
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

	// method save image when update product
	private List<Image> saveImage(List<MultipartFile> files, Product product) {
		List<String> listPath = cloudinaryServiceImp.upload(files);
		List<Image> savedImages = listPath.stream().map(path -> {
			Image image = new Image();
			image.setPath(path);
			image.setProduct(product);
			return image;
		}).collect(Collectors.toList());

		imageRepository.saveAll(savedImages);
		return savedImages;
	}

	// method update product quantity according to product size
	@Override
	public void updateProductQuantityForSize(int quantitySubtract, Long idProduct, Integer idSize) {
		Product product = productRepository.findById(idProduct)
				.orElseThrow(() -> new NotFoundException("Product not found!"));
		Size size = sizeRepository.findById(idSize).orElseThrow(() -> new NotFoundException("Size not found!"));

		ProductQuantity productQuantity = productQuantityRepository.findByProduct_IdAndSize_Id(idProduct, idSize);

		int quantityOld = productQuantity.getQuantity();

		if (quantitySubtract > quantityOld) {
			throw new RuntimeException("Quantity not enough!");
		}
		if (quantitySubtract > Constants.QUANTITY_PRODUCT_MAX_BUY) {
			throw new RuntimeException("Quantity must to less than or equal " + Constants.QUANTITY_PRODUCT_MAX_BUY);
		}
		productQuantity.setQuantity(quantityOld - quantitySubtract);
		productQuantity.setProduct(product);
		productQuantity.setSize(size);

	}
}
