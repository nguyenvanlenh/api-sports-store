package com.watermelon.model.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.watermelon.model.AbstractAuditEntity;

import jakarta.persistence.CascadeType;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractAuditEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	private String name;
	private String shortDescription;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	private BigDecimal salePrice;
	private BigDecimal regularPrice;
	private boolean isActive;
	private String thumbnailImage;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "brand_id")
	private Brand brand;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
	private Set<Rating> listRating = new HashSet<>();
	
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
	private Set<Image> listImages = new HashSet<>();
	
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
	private Set<ProductQuantity> quantityOfSizes = new HashSet<>();

	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
	private Set<OrderDetail> listOrderDetails = new HashSet<>();
	
	

}
