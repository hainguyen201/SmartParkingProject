package com.hust.smartparking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.smartparking.entity.User;
import com.hust.smartparking.service.impl.UserService;
import com.hust.smartparking.utils.Utils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
//    @PostMapping(value = "/users")
//    public ResponseEntity<User> addUser(@RequestParam(value = "user") User user){
//        return
//        ObjectMapper objectMapper = new ObjectMapper();
//        User user1= null;
//        try {
//            user1 = objectMapper.readValue(user, User.class);
//            if(Utils.checkValidFile(avatar)){
//
//                user1.setAvatar(avatar.getBytes());
//            }
//            user1.setCreatedDate(new Timestamp(new Date().getTime()));
//            return new ResponseEntity<>(userService.save(user1), HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//
//        }
//    }
    @PostMapping(value = "/users/search")
    public ResponseEntity<Iterable<User>> searchUser(@RequestBody User user){
        return new ResponseEntity<>(userService.searchData(user), HttpStatus.OK);

    }
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id,
                                           @RequestParam(value = "user") String user){
        ObjectMapper objectMapper = new ObjectMapper();
        User userUpdate= null;
        try {
            userUpdate = objectMapper.readValue(user, User.class);
            Optional<User> userOptional= userService.findById(id);
            User finalUserUpdate = userUpdate;
            return  userOptional.map(user1 -> {
                    finalUserUpdate.setPassword(user1.getPassword());
                    finalUserUpdate.setId(user1.getId());
                    finalUserUpdate.setModifiedDate(new Timestamp(new Date().getTime()));
                    return new ResponseEntity<>(userService.save(finalUserUpdate), HttpStatus.OK);
            }).orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

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
