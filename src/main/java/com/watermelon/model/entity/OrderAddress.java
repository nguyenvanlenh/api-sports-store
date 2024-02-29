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
	@Column(name = "contact_name")
	private String contactName;
	private String phone;
	@Column(name = "address_line1")
	private String addressLine1;
	@Column(name = "address_line2")
	private String addressLine2;
	private String city;
	private String zipcode;
	private String district;
	private String province;
	private String country;
	private Boolean active;
	

	
}
