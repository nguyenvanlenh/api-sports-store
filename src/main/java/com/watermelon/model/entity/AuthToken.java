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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auth_token")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken extends AbstractAuditEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Column(name = "refresh_token", columnDefinition = "TEXT")
	private String refreshToken;
	
	private boolean revoked;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
