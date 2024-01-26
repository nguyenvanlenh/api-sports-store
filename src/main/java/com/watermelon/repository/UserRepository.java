package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findByUsernameAndEmailAndPhone(String username, String email, String phone);

}
