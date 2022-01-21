package com.hust.smartparking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.smartparking.config.Constants;
import com.hust.smartparking.config.MqttGateway;
import com.hust.smartparking.entity.Gate;
import com.hust.smartparking.service.impl.GateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class GateController {
    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private GateService gateService;
    @GetMapping("/gates/{id}/{command}")
    public void controlGate(@PathVariable int id, @PathVariable int command) throws JsonProcessingException {
        //1 mở cổng, 0 đóng cổng
        Map<String, Integer> map = new HashMap<>();
        map.put("gate_id", id);
        map.put("status", command);
        ObjectMapper objectMapper= new ObjectMapper();
        mqttGateway.senToMqtt(objectMapper.writeValueAsString(map), Constants.topicGateServor);
    }
    @GetMapping("/gates")
    public ResponseEntity<Iterable<Gate>> getAllGate(){
        return new ResponseEntity<>(gateService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/gates/{id}")
    public ResponseEntity<Gate> getGateById(@PathVariable int id){
        Optional<Gate> gateOptional= gateService.findById(id);
        return gateOptional.map(gate -> new ResponseEntity<>(gate, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/gates")
    public ResponseEntity<Gate> addNewGate(@RequestBody Gate gate){
        gate.setCreatedDate(new Timestamp(new Date().getTime()));
        return new ResponseEntity<>(gateService.save(gate), HttpStatus.OK);
    }
    @PutMapping("/gates/{id}")
    public ResponseEntity<Gate> updateGate(@RequestBody Gate gate, @PathVariable int id){
        Optional<Gate> gateOptional= gateService.findById(id);
        return gateOptional.map(gate1 -> {
            gate.setGateId(gate1.getGateId());
            gate.setModifiedDate(new Timestamp(new Date().getTime()));
            return new ResponseEntity<>(gateService.save(gate), HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/gates/{id}")
    public ResponseEntity<Gate> deleteGate(@PathVariable int id) {
        Optional<Gate> gateOptional = gateService.findById(id);
        return gateOptional.map(gate -> {
            gateService.remove(id);
            return new ResponseEntity<>(gate, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
