package com.hust.smartparking.repository;

import com.hust.smartparking.entity.ParkingArea;
import com.hust.smartparking.entity.Vehicle;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface ParkingAreaRepository extends CrudRepository<ParkingArea, Integer> {
    Iterable<ParkingArea> findAll(Sort sort);
}
