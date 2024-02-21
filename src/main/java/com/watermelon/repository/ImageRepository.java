package com.watermelon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.Image;
@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	
	public List<Image> findByProduct_Id(Long productId);

}
