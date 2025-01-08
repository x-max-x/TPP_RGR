package com.tpp.rgrtpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpp.rgrtpp.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
}
