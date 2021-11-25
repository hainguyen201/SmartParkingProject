package com.hust.smartparking.controller;

import com.hust.smartparking.entity.Model;
import com.hust.smartparking.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
public class ModelController {
    @Autowired private ModelService modelService;
    @GetMapping("/models")
    public ResponseEntity<Iterable<Model>> findAll(){
        return new ResponseEntity<>(modelService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/models/{id}")
    public ResponseEntity<Model> findById(@PathVariable int id){
        Optional<Model> modelOptional= modelService.findById(id);
        return modelOptional.map(model -> new ResponseEntity<>(model, HttpStatus.OK))
                .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/models")
    public ResponseEntity<Model> save(@RequestBody Model model){
        model.setCreatedDate(new Timestamp(new Date().getTime()));
        return new ResponseEntity<>(modelService.save(model), HttpStatus.OK);
    }
    @PutMapping("/models/{id}")
    public  ResponseEntity<Model> updateModel(@PathVariable int id, @RequestBody Model model){
        Optional<Model> modelOptional= modelService.findById(id);
        return modelOptional.map(model1 -> {
            model.setId(model1.getId());
            model.setModifiedDate(new Timestamp(new Date().getTime()));
            return new ResponseEntity<>(modelService.save(model),HttpStatus.OK);
        }).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/models/{id}")
    public  ResponseEntity<Model> deleteModel(@PathVariable int id){
        Optional<Model> modelOptional= modelService.findById(id);
        return modelOptional.map(model1 -> {
            modelService.remove(id);
            return new ResponseEntity<>(model1, HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/models/status/{id}")
    public ResponseEntity<Model> updateModelStatus(@PathVariable int id){
        try {
            modelService.updateModelStatus(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
            modelService.updateModelStatus(id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
