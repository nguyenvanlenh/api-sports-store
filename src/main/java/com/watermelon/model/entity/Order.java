package com.watermelon.model.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.watermelon.model.AbstractAuditEntity;
import com.watermelon.model.enumeration.EDeliveryMethod;
import com.watermelon.model.enumeration.EDeliveryStatus;
import com.watermelon.model.enumeration.EOrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Orders")
@Getter
@Setter
public class Order extends AbstractAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "address_id")
	private OrderAddress orderAddress;
	private String nameCustomer;
	private String emailCustomer;
	private String phoneNumberCustomer;
	private BigDecimal totalPrice;
	private BigDecimal deliveryFee;

	@Enumerated(EnumType.STRING)
	private EOrderStatus orderStatus;
	@Enumerated(EnumType.STRING)
	private EDeliveryStatus deliveryStatus;
	@Enumerated(EnumType.STRING)
	private EDeliveryMethod deliveryMethod;
	private String couponCode;
	private String rejectReason;

	private Boolean active;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderDetail> listDetails = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToOne(mappedBy = "order")
	private Payment payment;

}
