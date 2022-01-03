package com.hust.smartparking.utils;

import com.hust.smartparking.entity.ParkingSlot;
import com.hust.smartparking.service.impl.ParkingSlotService;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

public class MQTTThread extends Thread{
    public static String topicServo="channel_hust/servor_sensor";
    public static String topicSonic="channel_hust/sonic_sensor";
    public static String broker="tcp://broker.mqttdashboard.com:1883";
    @Autowired private static ParkingSlotService parkingSlotService;
    public static  void handleParking(int position_id, int status){
        Optional<ParkingSlot> parkingSlotOptional=  parkingSlotService.findById(position_id);
        parkingSlotOptional.map(parkingSlot -> {
            parkingSlot.setStatus(status);
            return parkingSlotService.save(parkingSlot);
        });
    }
    @Override
    public void run() {
        String publisherId = UUID.randomUUID().toString();
        try {
            String content="open";
            MqttClient publisher = new MqttClient(broker,publisherId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            publisher.connect(connOpts);
            MqttMessage message = new MqttMessage(content.getBytes());
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
                            int status=jsonObject.getInt("status");
                            handleParking(position_id, status);



                        }else if( type==2)//cảm biến khoảng cách kiểm tra có xe
                        {

                        }else if(type==3)//Cảm biến khoảng cách để kiểm tra đóng cổng
                        {

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }




                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });

//            TimeUnit.SECONDS.sleep(5);
//            publisher.publish(topic, new MqttMessage("close".get));
//            System.out.println("Message published");
//            publisher.disconnect();
//            System.exit(0);

        } catch (MqttException  e) {
            e.printStackTrace();
        }
    }
}
