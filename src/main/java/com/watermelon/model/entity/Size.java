package com.watermelon.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.watermelon.model.AbstractAuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="sizes")
@Getter
@Setter
@NoArgsConstructor
public class Size extends AbstractAuditEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String name;
	@Column(name ="is_active")
	private boolean isActive;
	
	@OneToMany(mappedBy = "size")
	private Set<ProductQuantity> listProductQuantities = new HashSet<>();
}
