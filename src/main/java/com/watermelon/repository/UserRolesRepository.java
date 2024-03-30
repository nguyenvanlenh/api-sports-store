package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watermelon.model.entity.User;

public interface UserRolesRepository extends JpaRepository<User, Long>{
	
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM user_roles WHERE id_user = :userId")
	void deleteUserRoleByUserId(@Param("userId") Long userId);

}
