package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.Brand;
@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer>{

}
