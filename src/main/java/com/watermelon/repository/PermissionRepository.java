package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watermelon.model.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer>{
	
	Permission findByName(String name);

}
