package com.watermelon.model.entity;

import com.watermelon.event.listener.CartItemListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="Cart_Items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(CartItemListener.class)
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name="cart_id", nullable = false)
	private Cart cart;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="product_quantity_id")
	private ProductQuantity productQuantity;
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
