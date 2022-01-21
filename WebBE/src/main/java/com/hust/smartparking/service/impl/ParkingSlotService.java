package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.ParkingSlot;
import com.hust.smartparking.repository.ParkingSlotRepository;
import com.hust.smartparking.service.IParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingSlotService implements IParkingSlotService {
    @Autowired private ParkingSlotRepository parkingSlotRepository;
    @Override
    public Iterable<ParkingSlot> findAll() {
        return parkingSlotRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Optional<ParkingSlot> findById(int id) {
        return parkingSlotRepository.findById(id);
    }

    @Override
    public ParkingSlot save(ParkingSlot parkingSlot) {
        return parkingSlotRepository.save(parkingSlot);
    }

    @Override
    public void remove(int id) {
        parkingSlotRepository.deleteById(id);
    }
}
