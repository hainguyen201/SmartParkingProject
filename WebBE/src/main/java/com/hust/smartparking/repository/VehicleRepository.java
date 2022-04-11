package com.hust.smartparking.repository;

import com.hust.smartparking.entity.Vehicle;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {
    Iterable<Vehicle> findAll(Sort sort);
    
    @Query(value = "select count(1) from vehicles where exit_time is null", nativeQuery = true)
    Long getNumVehicleInside();
}
