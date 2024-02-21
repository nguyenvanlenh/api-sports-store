package com.watermelon.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.watermelon.model.AbstractAuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractAuditEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String name;
	@Column(name= "short_description")
	private String shortDescription;
	private String description;
	private String gtin;
	private String sku;
	private String slug;
	private BigDecimal price;
	@Max(value = 1)
	private double tax;
	@Column(name= "is_active")
	private boolean isActive;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany
	@JoinColumn(name = "product_id")
	private Set<Rating> listRating = new HashSet<>();
	
	@OneToMany
	@JoinColumn(name = "product_id")
	private Set<Image> listImages = new HashSet<>();
	
	@OneToMany
	@JoinColumn(name = "product_id")
	private Set<ProductQuantity> quantityOfSize = new HashSet<>();

	
	
	

}
