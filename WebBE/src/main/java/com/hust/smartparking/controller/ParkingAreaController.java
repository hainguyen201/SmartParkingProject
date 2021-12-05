package com.hust.smartparking.controller;

import com.hust.smartparking.entity.ParkingArea;
import com.hust.smartparking.entity.ParkingSlot;
import com.hust.smartparking.service.impl.ParkingAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
public class ParkingAreaController {
    @Autowired private ParkingAreaService parkingAreaService;
    @GetMapping("/parking_areas")
    public ResponseEntity<Iterable<ParkingArea>> getAll(){
        return new ResponseEntity<>(parkingAreaService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/parking_areas/{id}")
    public ResponseEntity<ParkingArea> getParkingAreaByid(@PathVariable int id){
        Optional<ParkingArea> parkingAreaOptional=parkingAreaService.findById(id);
        return parkingAreaOptional.map(parkingArea -> new ResponseEntity<>(parkingArea, HttpStatus.OK))
                .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/parking_areas")
    public ResponseEntity<ParkingArea> addParkingArea(@RequestBody ParkingArea parkingArea){
        parkingArea.setCreatedDate(new Timestamp(new Date().getTime()));
        return new ResponseEntity<>(parkingAreaService.save(parkingArea), HttpStatus.OK);
    }
    @PutMapping("/parking_areas/{id}")
    public ResponseEntity<ParkingArea> updateParkingArea(@PathVariable int id, @RequestBody ParkingArea parkingArea){
        Optional<ParkingArea> parkingAreaOptional=parkingAreaService.findById(id);
        return parkingAreaOptional.map(parkingArea1 -> {
            parkingArea.setModifiedDate(new Timestamp(new Date().getTime()));
            parkingArea.setId(parkingArea1.getId());
            return new ResponseEntity<>(parkingAreaService.save(parkingArea), HttpStatus.OK);
        }).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/parking_areas/{id}")
    public ResponseEntity<ParkingArea> deleteParkingArea(@PathVariable int id){
        Optional<ParkingArea> parkingAreaOptional=parkingAreaService.findById(id);
        return parkingAreaOptional.map(parkingArea -> {
            parkingAreaService.remove(id);
            return new ResponseEntity<>(parkingArea,HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
