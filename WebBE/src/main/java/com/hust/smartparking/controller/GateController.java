package com.hust.smartparking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.smartparking.config.Constants;
import com.hust.smartparking.config.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GateController {
    @Autowired
    private MqttGateway mqttGateway;
    @GetMapping("/gate/{id}/{command}")
    public void controlGate(@PathVariable int id, @PathVariable int command) throws JsonProcessingException {
        //1 mở cổng, 0 đóng cổng
        Map<String, Integer> map = new HashMap<>();
        map.put("gate_id", id);
        map.put("status", command);
        ObjectMapper objectMapper= new ObjectMapper();
        mqttGateway.senToMqtt(objectMapper.writeValueAsString(map), Constants.topicSensorSonicGate);
    }
}
