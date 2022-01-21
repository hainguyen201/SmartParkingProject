package com.hust.smartparking.service.impl;

import com.hust.smartparking.entity.Gate;
import com.hust.smartparking.repository.GateRepository;
import com.hust.smartparking.service.IGateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class GateService implements IGateService {
    @Autowired
    private GateRepository gateRepository;
    @Override
    public Iterable<Gate> findAll() {
        return gateRepository.findAll();
    }

    @Override
    public Optional<Gate> findById(int id) {
        return gateRepository.findById(id);
    }

    @Override
    public Gate save(Gate gate) {
        return gateRepository.save(gate);
    }

    @Override
    public void remove(int id) {
        gateRepository.deleteById(id);
    }
}
