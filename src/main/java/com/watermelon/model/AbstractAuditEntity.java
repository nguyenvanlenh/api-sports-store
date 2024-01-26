package com.watermelon.model;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AbstractAuditEntity.class)
public class AbstractAuditEntity {
	@CreationTimestamp
	@Column(name="created_on")
	private ZonedDateTime createdOn;
	@CreatedBy
	@Column(name="created_by")
	private String createdBy;
	@UpdateTimestamp
	@Column(name="last_mofified_on")
	private ZonedDateTime lastMofifiedOn;
	@LastModifiedBy
	@Column(name="last_modified_by")
	private String lastModifiedBy;
	

}
