package com.watermelon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsernameAndEmail(String username, String email);
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);

	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
