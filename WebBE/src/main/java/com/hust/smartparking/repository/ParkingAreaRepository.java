package com.hust.smartparking.repository;

import com.hust.smartparking.entity.ParkingArea;
import com.hust.smartparking.entity.Vehicle;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ParkingAreaRepository extends CrudRepository<ParkingArea, Integer> {
    Iterable<ParkingArea> findAll(Sort sort);
//    @Query(value = "select * from parking")
//    Iterable<ParkingArea> findByParkingSlot(int id);
}
