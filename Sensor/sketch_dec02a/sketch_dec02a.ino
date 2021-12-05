#include <Servo.h>     
//#define pos D1
Servo myservo; 

int pos = 0;

void setup() { 
    myservo.attach(D1);
} 

void loop() {
    myservo.write(0);
    delay(20);
    myservo.write(180); 
    delay(20);
}
