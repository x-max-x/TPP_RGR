package com.tpp.rgrtpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpp.rgrtpp.models.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
}
