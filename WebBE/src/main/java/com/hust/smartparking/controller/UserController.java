package com.hust.smartparking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.smartparking.entity.User;
import com.hust.smartparking.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
@RestController
public class UserController {
    @Autowired private UserService userService;
    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAll(){

        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getById(@PathVariable int id){
        Optional<User> userOptional=userService.findById(id);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping(value = "/users/")
    public ResponseEntity<User> addUser(@RequestParam(value = "user") String user,
                                        @RequestParam(value="avatar") MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        User user1= null;
        try {
            user1 = objectMapper.readValue(user, User.class);
            if(!file.isEmpty()){
                user1.setAvatar(file.getBytes());
            }
            user1.setCreatedDate(new Timestamp(new Date().getTime()));
            return new ResponseEntity<>(userService.save(user1), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user){
        Optional<User> userOptional= userService.findById(id);
        return userOptional.map(user1 -> {
            user.setId(user1.getId());
            user.setModifiedDate(new Timestamp(new Date().getTime()));
            return  new ResponseEntity<>(userService.save(user), HttpStatus.OK);
        }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id){
          Optional<User> userOptional= userService.findById(id);
          return userOptional.map(user -> {
              userService.remove(id);
              return new ResponseEntity<>(user, HttpStatus.OK);
          }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
