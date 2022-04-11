#define TRIG_PIN D1
#define ECHO_PIN D2

#define TRIG_PIN2 D7
#define ECHO_PIN2 D8

#define TRIG_PIN3 D6
#define ECHO_PIN3 D5
#define TIME_OUT 5000


// Thông tin về MQTT Broker
#define mqtt_server "192.168.1.4"
//#define mqtt_server "192.168.137.1"
#define mqtt_topic_pub_parking "channel_hust/sonic_parking"
#define mqtt_topic_pub_gate_camera "channel_hust/camera"
// #define mqtt_topic_pub_gate_after "channel/sonic_gate_after"
#define mqtt_topic_pub_gate_servo "channel_hust/servor_sensor"
#define mqtt_topic_sub "channel/gate1_in"
#define wifiID "A-11"
#include <Servo.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>
int status_gate_after=0;
int status_in=0;
int status_parking=0;
const int gate_id=20173089;
const int position_id=20173088;
// const char* ssid = "EXT";
// const char* password = "12345689";
long lastMsg = 0;
char msg[50];
int value = 0;
const uint16_t mqtt_port = 1883;
//Variables
int i = 0;
int statusCode;
const char* ssid = "Default_SSID";
const char* passphrase = "Default_Password";
String st;
String content;

Servo gateServo; 
int pos = 0;
WiFiClient espClient;
PubSubClient client(espClient);
ESP8266WebServer server(80);
// AsyncWebServer serverConfig(8080);


bool testWifi(void);
void launchWeb(void);
void setupAP(void);

float GetDistance()
{
  float duration, distanceCm;

  digitalWrite(TRIG_PIN, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);

  duration = pulseIn(ECHO_PIN, HIGH, TIME_OUT);

  // convert to distance
  distanceCm = duration / 29.1 / 2;

  return distanceCm;
}
float GetDistance2()
{
  float duration, distanceCm;

  digitalWrite(TRIG_PIN2, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG_PIN2, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN2, LOW);

  duration = pulseIn(ECHO_PIN2, HIGH, TIME_OUT);

  // convert to distance
  distanceCm = duration / 29.1 / 2;

  return distanceCm;
}
float GetDistance3()
{
  float duration, distanceCm;

  digitalWrite(TRIG_PIN3, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIG_PIN3, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN3, LOW);

  duration = pulseIn(ECHO_PIN3, HIGH, TIME_OUT);

  // convert to distance
  distanceCm = duration / 29.1 / 2;
  Serial.println("distance_after: "+String(distanceCm));
  return distanceCm;
}

void openGate(){
  for(; pos>=1; pos-=1){
    gateServo.write(pos);
    delay(5);
  }
}
void closeGate(){
  for(; pos<180; pos+=1){
    gateServo.write(pos);
    delay(5);
  }
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
  Serial.println(gate_id_rev);
  Serial.println(gate_id_rev==gate_id);
  if(gate_id_rev==gate_id){
    int status=doc["status"];
    if(status==0){
      closeGate();
    }
    if(status==1){
      openGate();
    }
  }
}


void reconnect() {
  // Chờ tới khi kết nối
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Thực hiện kết nối với mqtt user và pass
    if (client.connect("esp8266")) {
      Serial.println("connected");
      // Khi kết nối sẽ publish thông báo
      //client.publish(mqtt_topic_pub, "ESP_reconnected");
      // ... và nhận lại thông tin này
      client.subscribe(mqtt_topic_pub_gate_servo);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 1 seconds");
      // Đợi 5s
      delay(5000);
    }
  }
}
void setup() {
  gateServo.attach(D3);
  pinMode(TRIG_PIN, OUTPUT);
  pinMode(ECHO_PIN, INPUT);
  pinMode(TRIG_PIN2, OUTPUT);
  pinMode(ECHO_PIN2, INPUT);
  pinMode(TRIG_PIN3, OUTPUT);
  pinMode(ECHO_PIN3, INPUT);
  
//  pinMode (LED, OUTPUT);
  Serial.begin(115200);
  // setup_wifi();
  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);

  Serial.println("Disconnecting current wifi connection");
  WiFi.disconnect();
  EEPROM.begin(512); //Initialasing EEPROM
  delay(10);
  pinMode(LED_BUILTIN, OUTPUT);
  Serial.println();
  Serial.println();
  Serial.println("Startup");
  //---------------------------------------- Read eeprom for ssid and pass
  Serial.println("Reading EEPROM ssid");
  String esid;
  for (int i = 0; i < 32; ++i)
  {
    esid += char(EEPROM.read(i));
  }
  Serial.println();
  Serial.print("SSID: ");
  Serial.println(esid);
  Serial.println("Reading EEPROM pass");
  String epass = "";
  for (int i = 32; i < 96; ++i)
  {
    epass += char(EEPROM.read(i));
  }
  Serial.print("PASS: ");
  Serial.println(epass);
  WiFi.begin(esid.c_str(), epass.c_str());
  if (testWifi())
  {
    Serial.println("Succesfully Connected!!!");
    launchWeb();
    return;
  }
  else
  {
    Serial.println("Turning the HotSpot On");
    launchWeb();
    setupAP();// Setup HotSpot
  }
  Serial.println();
  Serial.println("Waiting.");
  while ((WiFi.status() != WL_CONNECTED))
  {
    Serial.print(".");
    delay(100);
    server.handleClient();
  }
}
void loop() {
  
  client.loop();
  if (!client.connected()) {
        reconnect();
  }
  long distance_after = GetDistance3();
  long distance = GetDistance();
  long distance_gate = GetDistance2();
  
  Serial.println(" gate after: "+String(distance_after));
  Serial.println(" parking slot: "+String(distance));
  Serial.println(" gate: "+String(distance_gate));
  delay(1000);
  long isVehicle= 0;
  //sensor kiểm tra xe đang ở vị trí ra vào
  if(distance_gate >10 && distance_gate <50){
    
    status_in+=1;
    //Nếu sau 500ms vẫn có xe thì sẽ gửi tín hiệu để lấy ảnh của xe
    if(status_in==2)
    {
      // Kiểm tra kết nối
      
      //publish vehicle entry
      StaticJsonDocument<256> doc;
      doc["gate_id"]=gate_id;
      doc["status"]=1;
      doc["value"]=distance_gate;
      char out[128];
      int b= serializeJson(doc, out);
      Serial.println("Publish message camera: ");
      Serial.println(out);
      client.publish(mqtt_topic_pub_gate_camera, out);
      delay(2000);
    }else{
      if(status_in==1){
        delay(500);
      }
    }
    delay(100);
  }else{
    status_in=0;
  }

  //sensor ở sau cổng
  if(distance_after >10 and distance_after <50){
    Serial.println("after: "+ String(distance_after));
    if(status_gate_after==0){
      delay(2000);
    }
    status_gate_after=1;
  }else{
    if(status_gate_after==1){
      if (!client.connected()) {
        reconnect();
      }
      Serial.println(distance_after);
      status_gate_after=0;
      delay(10);
      StaticJsonDocument<256> doc;
      doc["gate_id"]=gate_id;
      doc["status"]=0;
      doc["value"]=distance_after;
      char out[128];
      int b= serializeJson(doc, out);
      Serial.println("Publish message close servo: ");
      Serial.println(out);
      client.publish(mqtt_topic_pub_gate_servo, out);
//      delay(2000);
    }
    status_gate_after=0;
    
  }
  // tại vị trí đỗ
  if(distance >10 && distance <50){
    if(status_parking==0){
      status_parking=1;
      // Kiểm tra kết nối
      if (!client.connected()) {
        reconnect();
      }
      StaticJsonDocument<256> doc;
      doc["position_id"]=position_id;
      doc["status"]=1;
      doc["type"]=1;
      doc["value"]=distance;
      char out[128];
      int b= serializeJson(doc, out);
      Serial.println("Publish message parking: ");
      Serial.println(out);
      client.publish(mqtt_topic_pub_parking, out);
      delay(2000);
    }  
  }else{
    if(status_parking==1){
      status_parking=0;
      // Kiểm tra kết nối
      if (!client.connected()) {
        reconnect();
      }
      StaticJsonDocument<256> doc;
      doc["position_id"]=position_id;
      doc["status"]=0;
      doc["type"]=1;
      doc["value"]=distance;
      char out[128];
      int b= serializeJson(doc, out);
      Serial.println("Publish message parking: ");
      Serial.println(out);
      client.publish(mqtt_topic_pub_parking, out);
      delay(2000);
    }
  }


}
bool testWifi(void)
{
  int c = 0;
  Serial.println("Waiting for Wifi to connect");
  while ( c < 20 ) {
    if (WiFi.status() == WL_CONNECTED)
    {
      return true;
    }
    delay(500);
    Serial.print("*");
    c++;
  }
  Serial.println("");
  Serial.println("Connect timed out, opening AP");
  return false;
}
void launchWeb()
{
  Serial.println("");
  if (WiFi.status() == WL_CONNECTED)
    Serial.println("WiFi connected");
  Serial.print("Local IP: ");
  Serial.println(WiFi.localIP());
  Serial.print("SoftAP IP: ");
  Serial.println(WiFi.softAPIP());
  createWebServer();
  // Start the server
  server.begin();
  Serial.println("Server started");
}
void setupAP(void)
{
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  int n = WiFi.scanNetworks();
  Serial.println("scan done");
  if (n == 0)
    Serial.println("no networks found");
  else
  {
    Serial.print(n);
    Serial.println(" networks found");
    for (int i = 0; i < n; ++i)
    {
      // Print SSID and RSSI for each network found
      Serial.print(i + 1);
      Serial.print(": ");
      Serial.print(WiFi.SSID(i));
      Serial.print(" (");
      Serial.print(WiFi.RSSI(i));
      Serial.print(")");
      Serial.println((WiFi.encryptionType(i) == ENC_TYPE_NONE) ? " " : "*");
      delay(10);
    }
  }
  Serial.println("");
  st = "<ol>";
  for (int i = 0; i < n; ++i)
  {
    // Print SSID and RSSI for each network found
    st += "<li>";
    st += WiFi.SSID(i);
    st += " (";
    st += WiFi.RSSI(i);
    st += ")";
    st += (WiFi.encryptionType(i) == ENC_TYPE_NONE) ? " " : "*";
    st += "</li>";
  }
  st += "</ol>";
  delay(100);
  WiFi.softAP(wifiID, "");
  Serial.println("Initializing_softap_for_wifi credentials_modification");
  launchWeb();
  Serial.println("over");
}
void createWebServer(){
  {
    server.on("/", []() {
      IPAddress ip = WiFi.softAPIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);
      content = "<!DOCTYPE HTML>\r\n<html>Welcome to Wifi Credentials Update page";
      content += "<form action=\"/scan\" method=\"POST\"><input type=\"submit\" value=\"scan\"></form>";
      content += ipStr;
      content += "<p>";
      content += st;
      content += "</p><form method='get' action='setting'><label>SSID: </label><input name='ssid' length=32><input name='pass' length=64><input type='submit'></form>";
      content += "</html>";
      server.send(200, "text/html", content);
    });
    server.on("/scan", []() {
      //setupAP();
      IPAddress ip = WiFi.softAPIP();
      String ipStr = String(ip[0]) + '.' + String(ip[1]) + '.' + String(ip[2]) + '.' + String(ip[3]);
      content = "<!DOCTYPE HTML>\r\n<html>go back";
      server.send(200, "text/html", content);
    });
    server.on("/setting", []() {
      String qsid = server.arg("ssid");
      String qpass = server.arg("pass");
      if (qsid.length() > 0 && qpass.length() > 0) {
        Serial.println("clearing eeprom");
        for (int i = 0; i < 96; ++i) {
          EEPROM.write(i, 0);
        }
        Serial.println(qsid);
        Serial.println("");
        Serial.println(qpass);
        Serial.println("");
        Serial.println("writing eeprom ssid:");
        for (int i = 0; i < qsid.length(); ++i)
        {
          EEPROM.write(i, qsid[i]);
          Serial.print("Wrote: ");
          Serial.println(qsid[i]);
        }
        Serial.println("writing eeprom pass:");
        for (int i = 0; i < qpass.length(); ++i)
        {
          EEPROM.write(32 + i, qpass[i]);
          Serial.print("Wrote: ");
          Serial.println(qpass[i]);
        }
        EEPROM.commit();
        content = "{\"Success\":\"saved to eeprom... reset to boot into new wifi\"}";
        statusCode = 200;
        ESP.reset();
      } else {
        content = "{\"Error\":\"404 not found\"}";
        statusCode = 404;
        Serial.println("Sending 404");
      }
      server.sendHeader("Access-Control-Allow-Origin", "*");
      server.send(statusCode, "application/json", content);
    });
  }
}
