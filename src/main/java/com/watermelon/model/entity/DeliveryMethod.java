package com.watermelon.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "delivery_method")
@Getter
public class DeliveryMethod {
	@Id
	private Integer id;
	private String name;
	

}
