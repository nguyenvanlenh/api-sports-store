package com.watermelon.model.entity;

import java.io.Serializable;
import java.util.Set;

import com.watermelon.model.AbstractAuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends AbstractAuditEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@Column(name = "is_active")
	private boolean isActive;
	
	@ManyToMany(mappedBy = "listRoles")
	private Set<User> listUsers;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_pers",
	joinColumns =@JoinColumn(name = "id_role"),
	inverseJoinColumns = @JoinColumn(name ="id_permission"))
	private Set<Permission> listPermissions;
	

}
