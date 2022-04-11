package com.hust.smartparking.controller;

import com.hust.smartparking.entity.ParkingArea;
import com.hust.smartparking.entity.ParkingSlot;
import com.hust.smartparking.entity.Vehicle;
import com.hust.smartparking.service.impl.ParkingAreaService;
import com.hust.smartparking.service.impl.ParkingSlotService;
import com.hust.smartparking.service.impl.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
public class ParkingAreaController {
    @Autowired private ParkingAreaService parkingAreaService;
    @Autowired private VehicleService vehicleService;
    @Autowired private ParkingSlotService parkingSlotService;
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
            parkingArea.setCreatedDate(parkingArea1.getCreatedDate());
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
    @GetMapping("/parking_areas/slot/{id}")
    public ResponseEntity<List<ParkingSlot>> getSlotsByParkingAreaID(@PathVariable int id){
        Iterable<ParkingSlot> parkingSlots= parkingSlotService.findAll();
        Iterator<ParkingSlot> parkingSlotIterator= parkingSlots.iterator();
        List<ParkingSlot> parkingSlotList= new ArrayList<>();
        Integer empty=0;
        while (parkingSlotIterator.hasNext()){
            ParkingSlot parkingSlot= parkingSlotIterator.next();
            if(parkingSlot.getParkingAreaId()==id){
                parkingSlotList.add(parkingSlot);
            }
        }
        return new ResponseEntity<>(parkingSlotList, HttpStatus.OK);
    }
    @GetMapping("/parking_areas/empty-slot/{id}")
    public ResponseEntity<Integer> getEmptySlot(@PathVariable int id){
        Iterable<ParkingSlot> parkingSlots= parkingSlotService.findAll();
        Iterator<ParkingSlot> parkingSlotIterator= parkingSlots.iterator();
        Integer empty=0;
        while (parkingSlotIterator.hasNext()){
            ParkingSlot parkingSlot= parkingSlotIterator.next();
            if(parkingSlot.getParkingAreaId()==id && parkingSlot.getStatus()==0){
                empty++;
            }
        }
        return new ResponseEntity<>(empty, HttpStatus.OK);
    }
    @GetMapping("/parking_areas/slot")
    public ResponseEntity<Integer> getAllEmptySlot(){
        Iterable<ParkingSlot> parkingSlots= parkingSlotService.findAll();
        Iterator<ParkingSlot> parkingSlotIterator= parkingSlots.iterator();
        Integer empty=0;
        while (parkingSlotIterator.hasNext()){
            ParkingSlot parkingSlot= parkingSlotIterator.next();
            if( parkingSlot.getStatus()==0 && parkingSlot.getType()==2){
                empty++;
            }
        }
        return new ResponseEntity<>(empty, HttpStatus.OK);
    }
    @PostMapping("/parking_areas/search")
    public ResponseEntity<Iterable<ParkingArea>> searchData(@RequestBody ParkingArea parkingArea){
        return new ResponseEntity<>(parkingAreaService.searchData(parkingArea), HttpStatus.OK);
    }
    @GetMapping("/parking_areas/search/parkingslot/{id}")
    public ResponseEntity<List<ParkingArea>> searchByParkingSlot(@PathVariable int id){
        List<ParkingArea> parkingAreaList = new ArrayList<>();
        ParkingArea parkingArea= parkingAreaService.findByParkingSlot(id);
        if(parkingArea!=null)
            parkingAreaList.add(parkingArea);
        return new ResponseEntity<>(parkingAreaList, HttpStatus.OK);
    }

}
