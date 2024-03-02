package com.watermelon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.ProductQuantity;
@Repository
public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Long>{

	List<ProductQuantity> findByProduct_Id(Long id);
	ProductQuantity findByProduct_IdAndSize_Id(Long idProduct,Integer idSize);

}
