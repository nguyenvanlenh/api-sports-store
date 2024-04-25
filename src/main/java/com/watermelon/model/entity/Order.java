package com.watermelon.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.watermelon.model.AbstractAuditEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="Orders")
@Getter
@Setter
public class Order extends AbstractAuditEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_address")
	private OrderAddress orderAddress;
	private String note;
	private Double tax;
	private Double discount;
	@Column(name = "total_price")
	private Double totalPrice;
	@Column(name="dilivery_fee")
	private Double diliveryFee;
	
	@ManyToOne
	@JoinColumn(name="id_order_status")
	private OrderStatus orderStatus;
	@ManyToOne
	@JoinColumn(name="id_delivery_status")
	private DeliveryStatus deliveryStatus;
	@ManyToOne
	@JoinColumn(name="id_delivery_method")
	private DeliveryMethod deliveryMethod;
	@Column(name ="coupon_code")
	private String couponCode;
	@Column(name="reject_reason")
	private String rejectReason;
	
	private Boolean active;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderDetail> listDetails = new HashSet<>();
	

	

}
