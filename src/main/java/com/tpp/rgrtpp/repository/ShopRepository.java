package com.tpp.rgrtpp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpp.rgrtpp.models.Shop;


@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {
  
}

