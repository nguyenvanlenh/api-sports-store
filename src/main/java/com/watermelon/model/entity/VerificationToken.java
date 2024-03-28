package com.watermelon.model.entity;

import java.util.Calendar;
import java.util.Date;

import com.watermelon.utils.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "verification_token")
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String token;
	private Date expirationTime;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public VerificationToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
		this.expirationTime = this.getTokenExpirationTime();
	}

	public VerificationToken(String token) {
		super();
		this.token = token;
		this.expirationTime = this.getTokenExpirationTime();
	}

	private Date getTokenExpirationTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, Constants.EXPIRATION_TIME_MINUTE);
		return new Date(calendar.getTime().getTime());
	}

}
