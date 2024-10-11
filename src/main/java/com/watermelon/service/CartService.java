package com.watermelon.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.watermelon.dto.CartItemDTO;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.mapper.imp.CartMapper;
import com.watermelon.model.entity.Cart;
import com.watermelon.model.entity.CartItem;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.ProductQuantity;
import com.watermelon.model.entity.User;
import com.watermelon.repository.CartItemRepository;
import com.watermelon.repository.CartRepository;
import com.watermelon.repository.ProductQuantityRepository;
import com.watermelon.repository.ProductRepository;
import com.watermelon.repository.UserRepository;
import com.watermelon.utils.AuthenticationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

	CartRepository cartRepository;
	CartItemRepository cartItemRepository;
	UserRepository userRepository;
	ProductRepository productRepository;
	ProductQuantityRepository productQuantityRepository;
	CartMapper cartMapper;

	public void deleteCart() {
		Long userId = AuthenticationUtils.extractUserId();
		cartRepository.deleteByUserId(userId);
	}
	
	public CartItemDTO addCartItem(CartItemDTO cartItemAdd) {
		Long userId = AuthenticationUtils.extractUserId();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", userId));
		Cart cartExists = cartRepository.findByUserId(userId)
				.orElseGet(() ->  cartRepository.save(Cart.builder()
			                .user(user)
			                .isActive(true)
			                .build()));
		
		if(!cartItemRepository.existsByProductIdAndProductQuantitySizeId(
				AuthenticationUtils.extractUserId(),
				cartItemAdd.size().id())) {
			Product product = productRepository.findById(cartItemAdd.product().id())
					.orElseThrow(()-> new ResourceNotFoundException("PRODUCT_NOT_FOUND",
							cartItemAdd.product().id()));
			
			ProductQuantity productQuantity = productQuantityRepository.
					findByProductIdAndSizeId(product.getId(), cartItemAdd.size().id());
			
			CartItem cartItem = CartItem.builder()
					.cart(cartExists)
					.quantity(cartItemAdd.quantity())
					.product(product)
					.productQuantity(productQuantity)
					.build();
			
			return cartMapper.toDTO(cartItemRepository.save(cartItem));
		}
		return null;
	}
	public void removeCartItem(String cartItemId) {
		cartItemRepository.deleteById(cartItemId);
	}

	public void removeListCartItem(String[] listCartItemId) {
		if (listCartItemId != null && listCartItemId.length != 0)
			Arrays.stream(listCartItemId).forEach(itemId -> cartItemRepository.deleteById(itemId));
	}
	
	
	public void updateProductQuantityCartItem(String cartItemId, int quantity) {
		CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found", cartItemId));
		cartItem.setQuantity(quantity);
		cartItemRepository.save(cartItem);
	}
	
	public List<CartItemDTO> getCartByUserId(){
		 Long userId = AuthenticationUtils.extractUserId();
		    Cart cart = cartRepository.findByUserId(userId)
		            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user", userId));

		    return cart.getListCartItems().stream()
		            .map(cartMapper::toDTO)
		            .toList();
	}
}
