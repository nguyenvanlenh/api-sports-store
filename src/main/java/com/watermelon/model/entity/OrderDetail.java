package com.watermelon.model.entity;

import com.watermelon.model.AbstractAuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name ="Order_details")
@Data
public class OrderDetail extends AbstractAuditEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Product product;
	@ManyToOne
	private Order order;
	
	private Integer quantity;
	private String note;
	@Column(name="discount_amount")
	private Double discountAmount;
	private Double price;
	private String size;
	private String categogy;
	private String brand;
	
	@Column(name = "tax_percent")
	private String taxPercent;
	

}
