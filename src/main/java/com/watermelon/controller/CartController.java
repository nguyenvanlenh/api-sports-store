package com.watermelon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ulisesbocchio.jasyptspringboot.util.Collections;
import com.watermelon.dto.CartItemDTO;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.service.CartRedisService;
import com.watermelon.service.CartService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/carts")
public class CartController {

    CartService cartService;
    CartRedisService cartRedisService;

    // Lấy giỏ hàng của người dùng hiện tại
    @GetMapping
    public ResponseData<List<CartItemDTO>> getCartByUserId() throws JsonMappingException, JsonProcessingException {
        List<CartItemDTO> cartItems = cartRedisService.getCartItems();
        if(CollectionUtils.isEmpty(cartItems)) {
        	cartItems = cartService.getCartByUserId();
        	cartItems.forEach(cartRedisService::addCartItem);
        }
        
        return ResponseData.<List<CartItemDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Cart items retrieved successfully")
                .data(cartItems)
                .build();
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping
    public ResponseData<CartItemDTO> addCartItem(@Valid @RequestBody CartItemDTO cartItem) {
    	CartItemDTO cartItemId = cartService.addCartItem(cartItem);
        return ResponseData.<CartItemDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Item added to cart successfully")
                .data(cartItemId)
                .build();
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PatchMapping("/{cartItemId}")
    public ResponseData<Void> updateProductQuantityCartItem(@PathVariable String cartItemId,
                                                            @RequestBody int quantity) {
        cartService.updateProductQuantityCartItem(cartItemId, quantity);
        return ResponseData.<Void>builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Cart item quantity updated successfully")
                .build();
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/{cartItemId}")
    public ResponseData<Void> removeCartItem(@PathVariable String cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Cart item removed successfully")
                .build();
    }
    @DeleteMapping
    public ResponseData<Void> removeListCartItem(@RequestBody String[]  listCartItemId) {
        cartService.removeListCartItem(listCartItemId);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Cart item removed successfully")
                .build();
    }

    // Xóa toàn bộ giỏ hàng của người dùng
    @DeleteMapping("/clear")
    public ResponseData<Void> deleteCartByUserId() {
        cartService.deleteCart();
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Cart cleared successfully")
                .build();
    }
}
