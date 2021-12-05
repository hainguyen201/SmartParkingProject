package com.hust.smartparking.controller;

import com.hust.smartparking.entity.Device;
import com.hust.smartparking.service.impl.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
public class DeviceController {
    @Autowired private DeviceService deviceService;
    @GetMapping("/devices")
    public ResponseEntity<Iterable<Device>> getAll(){
        return new ResponseEntity<>(deviceService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/devices/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable int id){
        Optional<Device> deviceOptional=deviceService.findById(id);
        return deviceOptional.map(device -> new ResponseEntity<>(device, HttpStatus.OK))
                .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/devices")
    public ResponseEntity<Device> addDevice(@RequestBody Device device){
        device.setCreatedDate(new Timestamp(new Date().getTime()));
        return new ResponseEntity<>(deviceService.save(device), HttpStatus.OK);
    }
    @PutMapping("/devices/{id}")
    public ResponseEntity<Device> updateDevice(@RequestBody Device device, @PathVariable int id){
        Optional<Device> optionalDevice= deviceService.findById(id);
        return  optionalDevice.map(device1 -> {
            device.setCreatedDate(device1.getCreatedDate());
            device.setId(device1.getId());
            device.setModifiedDate(new Timestamp(new Date().getTime()));
            return new ResponseEntity(deviceService.save(device), HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Device> deleteDevice(@PathVariable int id){
        Optional<Device> deviceOptional= deviceService.findById(id);
        return deviceOptional.map(device -> {
            deviceService.remove(id);
            return new ResponseEntity<>(device, HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/devices/search")
    public ResponseEntity<Iterable<Device>> searchDevices(@RequestBody Device device){
        return new ResponseEntity<>(deviceService.searchData(device), HttpStatus.OK);
    }
}
