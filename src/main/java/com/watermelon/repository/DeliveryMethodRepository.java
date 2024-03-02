package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watermelon.model.entity.DeliveryMethod;

public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethod, Integer>{

}
