package com.hust.smartparking.controller;

import com.hust.smartparking.entity.ParkingSlot;
import com.hust.smartparking.service.impl.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
public class ParkingSlotController {
    @Autowired
    private ParkingSlotService parkingSlotService;
    @GetMapping("/parking_slots")
    public ResponseEntity<Iterable<ParkingSlot>> getAllParkingSlots(){
        return new ResponseEntity<>(parkingSlotService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/parking_slots/{id}")
    public ResponseEntity<ParkingSlot> getParkingSlotById(@PathVariable int id){
        Optional<ParkingSlot> optionalParkingSlot= parkingSlotService.findById(id);
        return optionalParkingSlot.map(parkingSlot -> new ResponseEntity<>(parkingSlot, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/parking_slots")
    public ResponseEntity<ParkingSlot> addParkingSlot(@RequestBody ParkingSlot parkingSlot){
        parkingSlot.setCreatedDate(new Timestamp(new Date().getTime()));
        return new ResponseEntity<>(parkingSlotService.save(parkingSlot), HttpStatus.OK);
    }
    @PutMapping("/parking_slots/{id}")
    public ResponseEntity<ParkingSlot> updateParkingSlot(@RequestBody ParkingSlot parkingSlot, @PathVariable int id){
        Optional<ParkingSlot> parkingSlotOptional= parkingSlotService.findById(id);
        return parkingSlotOptional.map(parkingSlot1 -> {
            parkingSlot.setId(parkingSlot1.getId());
            return new ResponseEntity<>(parkingSlotService.save(parkingSlot), HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/parking_slots/area/{id}")
    public ResponseEntity<ParkingSlot> updateParkingSlot(@PathVariable int id){
        Optional<ParkingSlot> parkingSlotOptional= parkingSlotService.findById(id);
        return parkingSlotOptional.map(parkingSlot -> {
            parkingSlot.setParkingAreaId(null);
            return new ResponseEntity<>(parkingSlotService.save(parkingSlot), HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/parking_slots/{id}")
    public ResponseEntity<ParkingSlot> deleteParkingSlot(@PathVariable int id){
        Optional<ParkingSlot> parkingSlotOptional=parkingSlotService.findById(id);
        return parkingSlotOptional.map(parkingSlot -> {
            parkingSlotService.remove(id);
            return new ResponseEntity<>(parkingSlot, HttpStatus.OK);
        }).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/parking_slot/status/{id}/{status}")
    public ResponseEntity<ParkingSlot> updateParkingStatus(@PathVariable int id, @PathVariable int status){
        Optional<ParkingSlot> parkingSlotOptional =parkingSlotService.findById(id);
        return parkingSlotOptional.map(parkingSlot -> {
            parkingSlot.setStatus(status);
            return new ResponseEntity<>(parkingSlotService.save(parkingSlot), HttpStatus.OK);

        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
