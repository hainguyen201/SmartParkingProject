package com.hust.smartparking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.smartparking.config.MqttGateway;
import com.hust.smartparking.entity.Model;
import com.hust.smartparking.entity.Vehicle;
import com.hust.smartparking.service.impl.ModelService;
import com.hust.smartparking.service.impl.VehicleService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CameraUploadImageController {
//    private final IStorageService storageService;
    @Autowired private ModelService modelService;
    @Autowired private VehicleService vehicleService;
    @Autowired private MqttGateway mqttGateway;
    private String topicServo="channel_hust/servor_sensor";
    @PostMapping("/models/test")
    public ResponseEntity<Map> testModel(@RequestParam("image") MultipartFile file,
                                         @RequestParam("modelurl")String modelUrl){
        try {
            String result= predictLicense(file, modelUrl);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map= mapper.readValue(result, Map.class);
            return new ResponseEntity<>(map,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/camera-upload")
    public ResponseEntity<Vehicle> upLoadImage(@RequestParam("image")MultipartFile imageFile,
//                                                @RequestParam("exitImage")MultipartFile exitImageFile,
                                              @RequestParam(value = "type", required = false)Long type,
                                              @RequestParam(value="gateType", required = false)Long gateType,
                                              @RequestParam(value = "gateId", required = false) int gateId){
        try {
            String modelURL= "";
            Iterable<Model> models= modelService.findAll();
            //tìm mô hình đang được sử dụng
            for (Model model: models) {
                if(model.getStatus()==1){
                    modelURL=model.getUrl();
                    break;
                }
            }
            if(imageFile!=null && !imageFile.isEmpty()){
                String result= predictLicense(imageFile, modelURL);
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map= mapper.readValue(result, Map.class);
                if(gateType==1)//xe vào
                {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setEntranceImage(imageFile.getBytes());
                    vehicle.setEntranceTime(new Timestamp(new Date().getTime()));
                    vehicle.setLicenseNumber(map.get("code"));
                    vehicle.setType(type);
                    openGate(gateId);
                    return new ResponseEntity<>(vehicleService.save(vehicle),HttpStatus.OK );
                }else if(gateType==2)//xe ra
                {
                    List<Vehicle> vehicleOptional = (List<Vehicle>) vehicleService.getLicense(map.get("code"), type);
                    if(vehicleOptional.size()==1){
                        Vehicle vehicle= vehicleOptional.get(0);
                        vehicle.setExitTime(new Timestamp(new Date().getTime()));
                        vehicle.setExitImage(imageFile.getBytes());
                        openGate(gateId);
                        return new ResponseEntity<>(vehicleService.save(vehicle),HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                }
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    public synchronized File convertFile(MultipartFile file) throws IOException {
        File convFile=  new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos= new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    public String predictLicense(MultipartFile file, String url) throws IOException {
        CloseableHttpClient client= HttpClients.createDefault();
        File newFile= convertFile(file);
//        File newFile= file.transferTo();
        HttpPost httpPost= new HttpPost(url);
        FileBody fileBody= new FileBody(newFile, ContentType.DEFAULT_BINARY);
        MultipartEntityBuilder builder= MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("image", fileBody);
        HttpEntity entity= builder.build();
        httpPost.setEntity(entity);
        HttpResponse response= null;
        try {
            response = client.execute(httpPost);
            System.out.println(response.getEntity().toString());
            String result= EntityUtils.toString(response.getEntity(), "utf-8");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            newFile.delete();
        }
    }
    public void openGate(int gateId) throws JsonProcessingException {
        Map<String, Integer> map = new HashMap<>();
        map.put("gate_id", gateId);
        map.put("status", 1);
        ObjectMapper objectMapper= new ObjectMapper();
        mqttGateway.senToMqtt(objectMapper.writeValueAsString(map), topicServo);
    }
}
