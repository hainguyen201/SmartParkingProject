#include <Servo.h>     
//#define pos D1
Servo gateServo; 

int pos = 0;

void setup() { 
    gateServo.attach(D8);
} 

void loop() {
    openGate();
    delay(5000);
    closeGate();
    delay(5000);
}
void openGate(){
  for(pos=179; pos>=1; pos-=1){
    gateServo.write(pos);
  }
}
void closeGate(){
  for(pos=0; pos<180; pos+=1){
    gateServo.write(pos);
    delay(15);
  }
}
