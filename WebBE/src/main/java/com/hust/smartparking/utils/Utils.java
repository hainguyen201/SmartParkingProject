package com.hust.smartparking.utils;

import com.hust.smartparking.entity.ParkingSlot;
import com.hust.smartparking.service.impl.ParkingSlotService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static RequestConfig requestConfig = RequestConfig.custom().build();
    public static String topicServo="channel_hust/servor_sensor";
    public static String topicSonic="channel_hust/sonic_sensor";
    public static String broker="tcp://broker.mqttdashboard.com:1883";
    public  static String baseUrl= "http://localhost:8089";
    public static boolean checkValidFile(MultipartFile file){
        if(file!=null && !file.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
    public static HttpResponse getLicenseAPI(String url, String fileUrl) throws IOException {
        HttpClient httpClient= HttpClientBuilder.create().
                setDefaultRequestConfig(requestConfig).build();
        HttpPost request = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().
                setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("image", new FileBody(new File(fileUrl)));
        HttpEntity entity = builder.build();
        request.setEntity(entity);
        return httpClient.execute(request);
    }

    public static  HttpResponse handleParking(String url) throws IOException {
        HttpClient httpClient= HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        HttpPut request= new HttpPut(url);
        request.setHeader("Content-type", "application/json");
        HttpResponse response= httpClient.execute(request);
        return response;

    }
    public static void main(String[] args) {
        String publisherId = UUID.randomUUID().toString();
        String subscriberId = UUID.randomUUID().toString();
        String subscriberId2 = UUID.randomUUID().toString();
        try {
            String content="open";
            MqttClient publisher = new MqttClient(broker,publisherId);
            MqttClient subscriber = new MqttClient(broker,subscriberId);
            MqttClient subscriber2 = new MqttClient(broker,subscriberId2);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            subscriber.connect(connOpts);
            subscriber2.connect(connOpts);
            publisher.connect(connOpts);
            MqttMessage message = new MqttMessage(content.getBytes());
            subscriber.subscribe(topicSonic);
            subscriber2.subscribe(topicSonic);
            subscriber2.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println("s2: "+mqttMessage.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            subscriber.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println("s1: "+mqttMessage.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });


        } catch (MqttException  e) {
            e.printStackTrace();
        }


    }
}