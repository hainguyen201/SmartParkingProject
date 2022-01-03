package com.hust.smartparking.repository;

import com.hust.smartparking.entity.ParkingSlot;
import com.hust.smartparking.entity.Vehicle;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface ParkingSlotRepository extends CrudRepository<ParkingSlot, Integer> {
    Iterable<ParkingSlot> findAll(Sort sort);
}
