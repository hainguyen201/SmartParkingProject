package com.hust.smartparking.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static RequestConfig requestConfig = RequestConfig.custom().build();
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

    public static void main(String[] args) throws IOException, ParseException {
//        HttpResponse response= getLicenseAPI("http://localhost:8787/license", "src/main/resources/targetFile.jpg");
//        System.out.println("myresponse: ");
//        System.out.println(response.toString());
//        System.out.println(new SimpleDateFormat("dd/MM/yyyy").parse("20/01/1999"));
        String publisherId = UUID.randomUUID().toString();
        try {
            String broker="tcp://broker.mqttdashboard.com:1883";
            String content="open";
            String topic="channel/gate1_in";
            MqttClient publisher = new MqttClient(broker,publisherId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            publisher.connect(connOpts);
            MqttMessage message = new MqttMessage(content.getBytes());
            publisher.publish(topic, message);
//            TimeUnit.SECONDS.sleep(5);
//            publisher.publish(topic, new MqttMessage("close".get));
            System.out.println("Message published");
            publisher.disconnect();
            System.exit(0);

        } catch (MqttException  e) {
            e.printStackTrace();
        }
        System.out.println(new Timestamp(new Date().getTime()));

    }
}