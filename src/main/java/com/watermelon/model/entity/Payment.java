package com.watermelon.model.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.watermelon.model.AbstractAuditEntity;
import com.watermelon.model.enumeration.EPaymentMethod;
import com.watermelon.model.enumeration.EPaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends AbstractAuditEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name="order_id")
	private Order order;
	private BigDecimal amount;
    private BigDecimal paymentFee;
    
    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private EPaymentStatus paymentStatus;
}
