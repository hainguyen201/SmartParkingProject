#include "esp_camera.h"
#include <Arduino.h>
#include "soc/soc.h"
#include "soc/rtc_cntl_reg.h"
#ifdef ESP32
  #include <WiFi.h>
  #include <AsyncTCP.h>
#else
  #include <ESP8266WiFi.h>
  #include <ESPAsyncTCP.h>
#endif
#include <ESPAsyncWebServer.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
//
// WARNING!!! PSRAM IC required for UXGA resolution and high JPEG quality
//            Ensure ESP32 Wrover Module or other board with PSRAM is selected
//            Partial images will be transmitted if image exceeds buffer size
//

// Select camera model
//#define CAMERA_MODEL_WROVER_KIT // Has PSRAM
//#define CAMERA_MODEL_ESP_EYE // Has PSRAM
//#define CAMERA_MODEL_M5STACK_PSRAM // Has PSRAM
//#define CAMERA_MODEL_M5STACK_V2_PSRAM // M5Camera version B Has PSRAM
//#define CAMERA_MODEL_M5STACK_WIDE // Has PSRAM
//#define CAMERA_MODEL_M5STACK_ESP32CAM // No PSRAM
#define CAMERA_MODEL_AI_THINKER // Has PSRAM
//#define CAMERA_MODEL_TTGO_T_JOURNAL // No PSRAM

#include "camera_pins.h"
#define mqtt_server "192.168.1.4"
#define mqtt_topic_pub_parking "channel_hust/sonic_parking"
#define mqtt_topic_pub_gate_camera "channel_hust/camera"
//WiFiClient espClient;
const uint16_t mqtt_port = 1883;
const char* ssid = "Wifi";
const char* password = "12345687";
String serverName = "192.168.1.4";
String serverPath = "/camera-upload";
int type=1;//1 xe máy, 2 ô tô
int gate_type=1;//1 xe vào, 2 xe ra
int gate_id=20173089;
int serverPort = 8089;  
int timerInterval = 3000;    // time between each HTTP POST image
String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjYW1lcmF0b2tlbiIsImlzQWRtaW4iOnRydWUsImV4cCI6MzIxNzQ4MTcwNiwiaWF0IjoxNjQwNjgxNzA2fQ.gLgSROlz8YKiZJE_3NPXaDmHs4MeU848tNuDXg4CYiSwlfUlllOnVGdbmCQURqUggCRrqlhwmZtHdpiCmu2a7g";
unsigned long previousMillis = 0;   // last time image was sent

//define webserver
AsyncWebServer server(8080);

const char* PARAM_INPUT_1 = "serverName";
const char* PARAM_INPUT_2 = "serverPath";
const char* PARAM_INPUT_3 = "serverPort";
const char* PARAM_INPUT_4 = "timerInterval";
const char* PARAM_INPUT_5 = "gateType";
const char* PARAM_INPUT_6 = "gateId";
// HTML web page to handle 3 input fields (serverName, serverPath, serverPort)
const char index_html[] PROGMEM = R"rawliteral(
<!DOCTYPE HTML><html><head>
  <title>ESP Input Form</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  </head><body>
  <form action="/get">
    serverName: <input type="text" style="width:100px" name="serverName">
    <input type="submit" value="Submit">
  </form><br>
  <form action="/get">
    serverPath: <input type="text" style="width:100px" name="serverPath">
    <input type="submit" value="Submit">
  </form><br>
  <form action="/get">
    serverPort: <input type="number" style="width:100px" name="serverPort">
    <input type="submit" value="Submit">
  </form><br>
  <form action="/get">
    timerInterval: <input type="text" style="width:100px" name="timerInterval">
    <input type="submit" value="Submit">
  </form><br>
  <form action="/get">
    Gate Type: <input type="text" style="width:100px" name="gateType">
    <input type="submit" value="Submit">
  </form><br>
  <form action="/get">
    Gate ID: <input type="text" style="width:100px" name="gateId">
    <input type="submit" value="Submit">
  </form><br>
</body></html>)rawliteral";

WiFiClient client;
WiFiClient client2;
PubSubClient clientMQTT(client2);
void reconnect() {
  // Chờ tới khi kết nối
  while (!clientMQTT.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Thực hiện kết nối với mqtt user và pass
    if (clientMQTT.connect("esp32")) {
      Serial.println("connected");
      // Khi kết nối sẽ publish thông báo
      //client.publish(mqtt_topic_pub, "ESP_reconnected");
      // ... và nhận lại thông tin này
      clientMQTT.subscribe(mqtt_topic_pub_gate_camera);
    } else {
      Serial.print("failed, rc=");
      Serial.print(clientMQTT.state());
      Serial.println(" try again in 1 seconds");
      // Đợi 5s
      delay(5000);
    }
  }
}

void notFound(AsyncWebServerRequest *request) {
  request->send(404, "text/plain", "Not found");
}

void startCameraServer();
String sendPhoto() {
  String getAll;
  String getBody;

  camera_fb_t * fb = NULL;
  fb = esp_camera_fb_get();
  if(!fb) {
    Serial.println("Camera capture failed");
    delay(1000);
    ESP.restart();
  }
  
  Serial.println("Connecting to server: " + serverName);

  if (client.connect(serverName.c_str(), serverPort)) {
    Serial.println("Connection successful!");    
    String head = "--HUST\r\nContent-Disposition: form-data; name=\"image\"; filename=\"image.jpg\"\r\nContent-Type: image/jpeg\r\n\r\n";
    String tail = "\r\n--HUST--\r\n";

    uint32_t imageLen = fb->len;
    uint32_t extraLen = head.length() + tail.length();
    uint32_t totalLen = imageLen + extraLen;
  
    client.println("POST " + serverPath+"?type="+String(type)+"&gateId="+ String(gate_id)+"&gateType="+ String(gate_type)+ " HTTP/1.1");
    client.println("Host: " + serverName);
    client.println("Content-Length: " + String(totalLen));
    client.println("Authorization: "+token);
    client.println("Content-Type: multipart/form-data; boundary=HUST");
    client.println();
    client.print(head);
  
    uint8_t *fbBuf = fb->buf;
    size_t fbLen = fb->len;
    for (size_t n=0; n<fbLen; n=n+1024) {
      if (n+1024 < fbLen) {
        client.write(fbBuf, 1024);
        fbBuf += 1024;
      }
      else if (fbLen%1024>0) {
        size_t remainder = fbLen%1024;
        client.write(fbBuf, remainder);
      }
    }   
    client.print(tail);
    
    esp_camera_fb_return(fb);
    
    int timoutTimer = 10000;
    long startTimer = millis();
    boolean state = false;
    
    while ((startTimer + timoutTimer) > millis()) {
      Serial.print(".");
      delay(100);      
      while (client.available()) {
        char c = client.read();
        if (c == '\n') {
          if (getAll.length()==0) { state=true; }
          getAll = "";
        }
        else if (c != '\r') { getAll += String(c); }
        if (state==true) { getBody += String(c); }
        startTimer = millis();
      }
      if (getBody.length()>0) { break; }
    }
    Serial.println();
    client.stop();
    Serial.println(getBody);
  }
  else {
    getBody = "Connection to " + serverName +  " failed.";
    Serial.println(getBody);
  }
  return getBody;
}
void callback(char* topic, byte* payload, unsigned int length) {
  char str[length+1];
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  int i=0;
  for (i=0;i<length;i++) 
    str[i]=(char)payload[i];
  str[i] = 0;
  Serial.println(str);
  StaticJsonDocument<256> doc;
  deserializeJson(doc,payload);
  int gate_id_rev=doc["gate_id"];
  int status= doc["status"];
  if(gate_id_rev==gate_id && status==1){
    sendPhoto();
  }
}
void setup() {
  Serial.begin(115200);
  Serial.setDebugOutput(true);
  Serial.println();

  camera_config_t config;
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = Y2_GPIO_NUM;
  config.pin_d1 = Y3_GPIO_NUM;
  config.pin_d2 = Y4_GPIO_NUM;
  config.pin_d3 = Y5_GPIO_NUM;
  config.pin_d4 = Y6_GPIO_NUM;
  config.pin_d5 = Y7_GPIO_NUM;
  config.pin_d6 = Y8_GPIO_NUM;
  config.pin_d7 = Y9_GPIO_NUM;
  config.pin_xclk = XCLK_GPIO_NUM;
  config.pin_pclk = PCLK_GPIO_NUM;
  config.pin_vsync = VSYNC_GPIO_NUM;
  config.pin_href = HREF_GPIO_NUM;
  config.pin_sscb_sda = SIOD_GPIO_NUM;
  config.pin_sscb_scl = SIOC_GPIO_NUM;
  config.pin_pwdn = PWDN_GPIO_NUM;
  config.pin_reset = RESET_GPIO_NUM;
  config.xclk_freq_hz = 20000000;
  config.pixel_format = PIXFORMAT_JPEG;
  
  // if PSRAM IC present, init with UXGA resolution and higher JPEG quality
  //                      for larger pre-allocated frame buffer.
  if(psramFound()){
    config.frame_size = FRAMESIZE_UXGA;
    config.jpeg_quality = 10;
    config.fb_count = 2;
  } else {
    config.frame_size = FRAMESIZE_XGA;
    config.jpeg_quality = 12;
    config.fb_count = 1;
  }

#if defined(CAMERA_MODEL_ESP_EYE)
  pinMode(13, INPUT_PULLUP);
  pinMode(14, INPUT_PULLUP);
#endif

  // camera init
  esp_err_t err = esp_camera_init(&config);
  if (err != ESP_OK) {
    Serial.printf("Camera init failed with error 0x%x", err);
    return;
  }

  sensor_t * s = esp_camera_sensor_get();
  // initial sensors are flipped vertically and colors are a bit saturated
  if (s->id.PID == OV3660_PID) {
    s->set_vflip(s, 1); // flip it back
    s->set_brightness(s, 1); // up the brightness just a bit
    s->set_saturation(s, -2); // lower the saturation
  }
  // drop down frame size for higher initial frame rate
  s->set_framesize(s, FRAMESIZE_QVGA);

#if defined(CAMERA_MODEL_M5STACK_WIDE) || defined(CAMERA_MODEL_M5STACK_ESP32CAM)
  s->set_vflip(s, 1);
  s->set_hmirror(s, 1);
#endif

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");

  startCameraServer();

  Serial.print("Camera Ready! Use 'http://");
  Serial.print(WiFi.localIP());
  Serial.println("' to connect");
  //setup webserver
  // Send web page with input fields to client
  server.on("/", HTTP_GET, [](AsyncWebServerRequest *request){
    request->send_P(200, "text/html", index_html);
  });

  // Send a GET request to <ESP_IP>/get?serverName=<inputMessage>
  server.on("/get", HTTP_GET, [] (AsyncWebServerRequest *request) {
    String inputMessage;
    String inputParam;
    // GET serverName value on <ESP_IP>/get?serverName=<inputMessage>
    if (request->hasParam(PARAM_INPUT_1)) {
      inputMessage = request->getParam(PARAM_INPUT_1)->value();
      serverName=inputMessage;
      inputParam = PARAM_INPUT_1;
    }
    // GET serverPath value on <ESP_IP>/get?serverPath=<inputMessage>
    else if (request->hasParam(PARAM_INPUT_2)) {
      inputMessage = request->getParam(PARAM_INPUT_2)->value();
      serverPath=inputMessage;
      inputParam = PARAM_INPUT_2;
    }
    // GET serverPort value on <ESP_IP>/get?serverPort=<inputMessage>
    else if (request->hasParam(PARAM_INPUT_3)) {
      inputMessage = request->getParam(PARAM_INPUT_3)->value();
      serverPort= inputMessage.toInt();
      inputParam = PARAM_INPUT_3;
    }
    else if (request->hasParam(PARAM_INPUT_4)) {
      inputMessage = request->getParam(PARAM_INPUT_4)->value();
      timerInterval=inputMessage.toInt();
      inputParam = PARAM_INPUT_4;
    }
    else if (request->hasParam(PARAM_INPUT_5)) {
      inputMessage = request->getParam(PARAM_INPUT_5)->value();
      gate_type=inputMessage.toInt();
      inputParam = PARAM_INPUT_5;
    }
    else if (request->hasParam(PARAM_INPUT_6)) {
      inputMessage = request->getParam(PARAM_INPUT_6)->value();
      gate_id=inputMessage.toInt();
      inputParam = PARAM_INPUT_6;
    }
    else {
      inputMessage = "No message sent";
      inputParam = "none";
    }
    Serial.println(inputMessage);
    request->send(200, "text/html", "HTTP GET request sent to your ESP on input field (" 
                                     + inputParam + ") with value: " + inputMessage +
                                     "<br><a href=\"/\">Return to Home Page</a>");
  });
  server.onNotFound(notFound);
  server.begin();
//  sendPhoto(); 
  clientMQTT.setServer(mqtt_server, mqtt_port);
  clientMQTT.setCallback(callback);
}

void loop() {
  // put your main code here, to run repeatedly:
//   unsigned long currentMillis = millis();
//   if (currentMillis - previousMillis >= timerInterval) {
//     sendPhoto();
//     previousMillis = currentMillis;
//   }
  clientMQTT.loop();
  if(!clientMQTT.connected()){
    reconnect();
  }
  
}
