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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
public class Rating extends AbstractAuditEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int star;
	private String content;
	@Column(name = "is_active")
	private boolean isActive;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;


}
