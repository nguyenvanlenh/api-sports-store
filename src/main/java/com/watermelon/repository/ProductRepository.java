package com.watermelon.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.isActive = true")
	Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("keyword") String keyword, Pageable pageable);

	Page<Product> findByCategory_UrlKeyAndIsActiveTrue(String urlKey, Pageable pageable);

	Optional<Product> findByIdAndIsActiveTrue(Long id);

	Page<Product> findByIsActiveTrue(Pageable pageable);

}
