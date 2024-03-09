package com.watermelon.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.Rating;
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>{

	
	Page<Rating> findByProduct_Id(Long product_Id,Pageable pageable);
	
	@Query(value = "SELECT SUM(r.star), COUNT(r) FROM Rating r WHERE r.product.id = :idProduct")
	List<Object[]> getTotalStarAndTotalRating(@Param("idProduct") Long idProduct);
}
