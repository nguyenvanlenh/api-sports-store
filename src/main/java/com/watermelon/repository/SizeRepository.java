package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.Size;
import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer>{

	List<Size> findByName(String name);
}
