package com.hust.smartparking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.smartparking.entity.Vehicle;
import com.hust.smartparking.service.impl.VehicleService;
import com.hust.smartparking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public ResponseEntity<Vehicle> addVehicle(@RequestParam(value = "vehicle", required = true) String vehicleString,
                                              @RequestParam(value = "entranceImage", required = false) MultipartFile entranceImage,
                                              @RequestParam(value = "exitImage", required = false) MultipartFile exitImage){
        ObjectMapper objectMapper = new ObjectMapper();
        Vehicle vehicle= null;

        try {
            vehicle = objectMapper.readValue(vehicleString, Vehicle.class);
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
    public ResponseEntity<Vehicle> updateVehicle(@RequestParam(value = "vehicle") String vehicleString,
                                                 @RequestParam(value = "entranceImage", required = false) MultipartFile entranceImage,
                                                 @RequestParam(value = "exitImage", required = false) MultipartFile exitImage,
                                                 @RequestParam(value ="exitImageDelete", required = false)Boolean exitImageDelete,
                                                 @RequestParam(value ="entranceImageDelete", required = false)Boolean entranceImageDelete,
                                                 @PathVariable int id){
        Optional<Vehicle> vehicleOptional= vehicleService.findById(id);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Vehicle vehicle= null;
            vehicle = objectMapper.readValue(vehicleString, Vehicle.class);
            Vehicle finalVehicle = vehicle;
            return vehicleOptional.map(vehicle1 -> {
                finalVehicle.setId(vehicle1.getId());
                finalVehicle.setExitImage(vehicle1.getExitImage());
                finalVehicle.setEntranceImage(vehicle1.getEntranceImage());
                try{
                    if(Utils.checkValidFile(entranceImage)){
                        finalVehicle.setEntranceImage(entranceImage.getBytes());
                    }else{
                        if(entranceImageDelete!=null && entranceImageDelete)
                            finalVehicle.setEntranceImage(null);
                    }
                    if(Utils.checkValidFile(exitImage)){
                        finalVehicle.setExitImage(exitImage.getBytes());
                    }else{
                        if(exitImageDelete!=null && exitImageDelete)
                            finalVehicle.setExitImage(null);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return new ResponseEntity<>(vehicleService.save(finalVehicle), HttpStatus.OK);

            }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @PostMapping("/vehicles/search")
    public ResponseEntity<Iterable<Vehicle>> searchData(@RequestParam("vehicle") String vehicle,
                                              @RequestParam("fromEtrance") String fromEntranceString,
                                              @RequestParam("toEntrance") String toEntranceString,
                                              @RequestParam("fromExit") String fromExitString,
                                              @RequestParam("toExit") String toExitString){
        ObjectMapper objectMapper = new ObjectMapper();
        Vehicle vehicle1= null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Timestamp fromEntrance=null;
        Timestamp toEntrance=null;
        Timestamp fromExit=null;
        Timestamp toExit=null;

        try {
            if(!"null".equals(fromEntranceString) && !fromEntranceString.isEmpty())
                fromEntrance = new Timestamp(dateFormat.parse(fromEntranceString).getTime());
            if(!"null".equals(toEntranceString) && !toEntranceString.isEmpty())
                toEntrance = new Timestamp(dateFormat.parse(toEntranceString).getTime());
            if(!"null".equals(fromExitString) && !fromExitString.isEmpty())
                fromExit = new Timestamp(dateFormat.parse(fromExitString).getTime());
            if(!"null".equals(toExitString) && !toExitString.isEmpty())
                toExit = new Timestamp(dateFormat.parse(toExitString).getTime());
            vehicle1 = objectMapper.readValue(vehicle, Vehicle.class);
            return new ResponseEntity<>(vehicleService.searchData(vehicle1, fromEntrance, toEntrance, fromExit, toExit), HttpStatus.OK);
        } catch (JsonProcessingException | ParseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @DeleteMapping(value = "/vehicles/{id}")
    public ResponseEntity<Vehicle> deleteVehicles(@PathVariable int id){
        Optional<Vehicle> vehicleOptional= vehicleService.findById(id);
        return vehicleOptional.map(model1 -> {
            vehicleService.remove(id);
            return new ResponseEntity<>(model1, HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


}
