package com.watermelon.model.entity;


import com.watermelon.model.AbstractAuditEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name= "product_quantities",uniqueConstraints = {
        @UniqueConstraint(columnNames = { "product_id", "size_id" })
})
@Getter
@Setter
public class ProductQuantity extends AbstractAuditEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Max(value = 1000)
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name = "size_id")
	private Size size;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	
	

}
