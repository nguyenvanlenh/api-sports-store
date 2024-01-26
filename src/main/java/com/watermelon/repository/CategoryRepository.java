package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.watermelon.model.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
