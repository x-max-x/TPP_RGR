package com.tpp.rgrtpp.service;

import com.tpp.rgrtpp.models.City;
import com.tpp.rgrtpp.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;
	

    public List<City> getAllCities() {
			return cityRepository.findAll(Sort.by(Sort.Order.asc("cityId")));
    }

    public Optional<City> findCityById(int id) {
        return cityRepository.findById(id);
    }

    public void saveCity(City city) {
        cityRepository.save(city);
    }

		public class CityHasShopsException extends RuntimeException {
			public CityHasShopsException(String message) {
					super(message);
			}
	}

    public void updateCity(City updatedCity) {
        City existingCity = cityRepository.findById(updatedCity.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        existingCity.setCityName(updatedCity.getCityName());
        existingCity.setPopulation(updatedCity.getPopulation());
        existingCity.setRegion(updatedCity.getRegion());

        cityRepository.save(existingCity);
    }

		public void deleteCity(int id) {
			cityRepository.deleteById(id);
	}
}
