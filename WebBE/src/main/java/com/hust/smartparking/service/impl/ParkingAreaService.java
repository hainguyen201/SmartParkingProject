package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.ParkingArea;
import com.hust.smartparking.repository.ParkingAreaRepository;
import com.hust.smartparking.service.IParkingAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ParkingAreaService implements IParkingAreaService {
    @Autowired private ParkingAreaRepository parkingAreaRepository;
    @Override
    public Iterable<ParkingArea> findAll() {
        return parkingAreaRepository.findAll();
    }

    @Override
    public Optional<ParkingArea> findById(int id) {
        return parkingAreaRepository.findById(id);
    }

    @Override
    public ParkingArea save(ParkingArea parkingArea) {
        return parkingAreaRepository.save(parkingArea);
    }

    @Override
    public void remove(int id) {
        parkingAreaRepository.deleteById(id);
    }
}
