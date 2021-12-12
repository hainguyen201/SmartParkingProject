package com.hust.smartparking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class CameraUploadImageController {
//    private final IStorageService storageService;
    @PostMapping("/entrances")
    public ResponseEntity<Object> upLoadImage(@RequestParam("entranceImage")MultipartFile file,
                                              @RequestParam("type")int type,
                                              @RequestParam(value = "gateId", required = false)String gateId){
        try {
            File filePath = new File("src/main/resources/targetFile.jpg");
            try (OutputStream os = new FileOutputStream(filePath)) {
                 os.write(file.getBytes());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
