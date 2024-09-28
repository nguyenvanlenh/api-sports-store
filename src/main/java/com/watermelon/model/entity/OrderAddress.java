package com.watermelon.model.entity;

import com.watermelon.model.AbstractAuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Order_Addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddress extends AbstractAuditEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "address_line")
	private String addressLine;
	private String commune;
	private String district;
	private String province;
	private String country;
	private Boolean active;
	

	
}
