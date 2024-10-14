package com.watermelon.model.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="Carts")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Builder.Default
	@OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, 
	cascade = {CascadeType.MERGE,CascadeType.MERGE},
	orphanRemoval = true)
	private Set<CartItem> listCartItems = new HashSet<>();
	
	private boolean isActive;
}
