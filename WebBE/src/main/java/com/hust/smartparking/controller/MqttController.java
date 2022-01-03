package com.hust.smartparking.controller;

import org.apache.http.client.config.RequestConfig;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MqttController {
    private static RequestConfig requestConfig = RequestConfig.custom().build();
    public static String topicServo="channel_hust/servor_sensor";
    public static String topicCamera="channel_hust/camera";
    public static String topicSonic="channel_hust/sonic_sensor";
    public static String broker="tcp://broker.mqttdashboard.com:1883";
    public  static String baseUrl= "http://localhost:8089";

    @GetMapping("/subscribe")
    public ResponseEntity<String> subscribe(){
        try{
            String publisherId = UUID.randomUUID().toString();
            String content="open";
            MqttClient publisher = new MqttClient(broker,publisherId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            publisher.connect(connOpts);
            publisher.subscribe(topicSonic);
            publisher.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                    JSONObject jsonObject = new JSONObject(mqttMessage.toString());
                    int type=jsonObject.getInt("type");
                    try{
                        if(type==1)//cảm biển khoảng cách ở vị trí đỗ
                        {
                            int position_id=jsonObject.getInt("position_id");
                            int status= jsonObject.getInt("status");
                            String url= baseUrl+"/parking_slot/status/"+position_id+"/"+status;
//                            handleParking(url);
                        }
//                        else if( type==2)//cảm biến khoảng cách kiểm tra có xe
//                        {
//                            int status=jsonObject.getInt("status");
//                            if(status==1){
//                                int gate_id= jsonObject.getInt("gate_id");
//                                int vehicle_type=jsonObject.getInt("vehicle_type");
//                                int gate_type= jsonObject.getInt("gate_type");
//                                publisher.publish(topicCamera, new MqttMessage("publish".getBytes()));
//                            }
//                        }else if(type==3)//Cảm biến khoảng cách để kiểm tra đóng cổng
//                        {
//
//                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
