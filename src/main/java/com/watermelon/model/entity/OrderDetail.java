package com.watermelon.model.entity;


import com.watermelon.model.AbstractAuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="Order_details")
@Getter
@Setter
public class OrderDetail extends AbstractAuditEntity{
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	@ManyToOne
	@JoinColumn(name = "order_id")
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
