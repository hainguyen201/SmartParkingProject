package com.hust.smartparking.controller;

import com.hust.smartparking.entity.Vehicle;
import com.hust.smartparking.service.impl.VehicleService;
import com.hust.smartparking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
public class VehicleController {
    @Autowired private VehicleService vehicleService;
    @GetMapping("/vehicles")
    public ResponseEntity<Iterable<Vehicle>> getAll(){
        return new ResponseEntity<>(vehicleService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable int id){
        Optional<Vehicle> optionalVehicle=vehicleService.findById(id);
        return optionalVehicle.map(vehicle ->
                new ResponseEntity<>(vehicle, HttpStatus.OK)).
                orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> addVehicle(Vehicle vehicle,
                                              @RequestParam(value = "entranceImage") MultipartFile entranceImage,
                                              @RequestParam(value = "exitImage") MultipartFile exitImage){
        try {
            if(Utils.checkValidFile(entranceImage)){
                vehicle.setEntranceImage(entranceImage.getBytes());
            }
            if(Utils.checkValidFile(exitImage)){
                vehicle.setExitImage(exitImage.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(vehicleService.save(vehicle), HttpStatus.OK);
    }
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@RequestBody Vehicle vehicle,
                                                 @RequestParam(value = "entranceImage") MultipartFile entranceImage,
                                                 @RequestParam(value = "exitImage") MultipartFile exitImage,
                                                 @PathVariable int id){
        Optional<Vehicle> vehicleOptional= vehicleService.findById(id);
        return vehicleOptional.map(vehicle1 -> {
            vehicle.setId(vehicle1.getId());
            try{
                if(Utils.checkValidFile(entranceImage)){
                    vehicle.setEntranceImage(entranceImage.getBytes());
                }
                if(Utils.checkValidFile(exitImage)){
                    vehicle.setExitImage(exitImage.getBytes());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return new ResponseEntity<>(vehicleService.save(vehicle), HttpStatus.OK);

        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }


}
