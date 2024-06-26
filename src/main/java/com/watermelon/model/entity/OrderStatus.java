package com.watermelon.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Entity
@Table(name = "order_status")
@Getter
public class OrderStatus {
	@Id
	private Integer id;
	@NotBlank(message="Name order status is required")
	private String name;
	

}
