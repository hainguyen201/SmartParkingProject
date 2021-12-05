package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.Vehicle;
import com.hust.smartparking.repository.VehicleRepository;
import com.hust.smartparking.service.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleService implements IVehicleService {
    @Autowired private VehicleRepository vehicleRepository;

    @Override
    public Iterable<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public void remove(int id) {
        vehicleRepository.deleteById(id);
    }
}
