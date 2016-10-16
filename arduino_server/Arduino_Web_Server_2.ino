/*
 * @author: Ankit Sharma
 * A Web Server application to be controlled from Intel Edison
 * It manipulates the lights on TILED device.
 */
#include <SPI.h>
#include <Ethernet.h>

void setMode();
void setLedDefaultState();
void turnItOff();
void setNormalMode();
void setRomanticMode();
void setPartyMode();
void setScatter();

// Define LED pins
int led1 = 5;
int led2 = 6;
int led3 = 9;
//int led4 = 10
int led5 = 11;
//int led6 = 13;


// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[] = {
  0X90, 0xA2, 0xDA, 0x0F, 0x25, 0xB3
};
byte ip[] = {192, 168, 1, 115};

byte gateway[] = {192, 168, 1, 1};

byte subnet[] = {255, 255, 255, 0};

// Initialize the Ethernet server library
// with the IP address and port you want to use
// (port 80 is default for HTTP):
EthernetServer server(8080);

void setup() {
  setMode();
  setLedDefaultState();
  // Open serial communications and wait for port to open:
  //Serial.begin(9600);
  //while (!Serial) {
  //  ; // wait for serial port to connect. Needed for native USB port only
  
  
  // start the Ethernet connection and the server:
  Ethernet.begin(mac, ip, gateway, subnet);
  server.begin();
  //Serial.print("server is at ");
  //Serial.println(Ethernet.localIP());
}


void loop() {
  // listen for incoming clients
  EthernetClient client = server.available();
  delay(1);
  //Serial.println("Client available");
  if (client) {
    //Serial.println("Client Exists");
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        if (c == '2') {
          turnItOff();
        } else if (c == '6') {
          setNormalMode();
        } else if (c == '7') {
          setRomanticMode();
        } else if (c == '8') {
          setPartyMode();
        } else if (c == '9') {
          setScatter();
        }
        //Serial.write(c);
        break;
        }
      }
    }
    // give the web browser time to receive the data
    //delay(1);
    // close the connection:
    client.stop();
    //Serial.println("client disconnected");
}

// turn off the led state
void turnItOff() {
  digitalWrite(led1, LOW);
  digitalWrite(led2, LOW);
  digitalWrite(led3, LOW);
  digitalWrite(led5, LOW);
}

// sets the initial mode of the leds
void setMode() {
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led3, OUTPUT);
  pinMode(led5, OUTPUT);
  //pinMode(led6, OUTPUT);
}

// sets the initial value for the leds
void setLedDefaultState() {
  digitalWrite(led1, LOW);
  digitalWrite(led2, LOW);
  digitalWrite(led3, LOW);
  digitalWrite(led5, LOW);
  //digitalWrite(led6, LOW);
}

// sets the normal mode
void setNormalMode() {
  digitalWrite(led1, HIGH);
  digitalWrite(led2, HIGH);
  digitalWrite(led3, HIGH);
  digitalWrite(led5, HIGH);
}

// sets the romantic mode
void setRomanticMode() {
  digitalWrite(led1, LOW);
  analogWrite(led2, 32);
  digitalWrite(led3, LOW);
  digitalWrite(led5, LOW);
}

// sets the party mode
void setPartyMode() {
  digitalWrite(led1, HIGH);
  digitalWrite(led2, HIGH);
  digitalWrite(led3, HIGH);
  digitalWrite(led5, LOW);
}

void setScatter() {
  digitalWrite(led1, HIGH);
  digitalWrite(led2, LOW);
  digitalWrite(led3, LOW);
  digitalWrite(led5, LOW);  
}

