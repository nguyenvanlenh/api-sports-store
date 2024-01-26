package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.ProductQuantity;
@Repository
public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Long>{

}
