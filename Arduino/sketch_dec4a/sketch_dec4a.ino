#include <OneWire.h>
#include <DallasTemperature.h>

// Pin definitions
const int motorEnablePin = 9;  // Enable pin for L293D
const int motorIn1 = 7;        // IN1 pin for L293D
const int motorIn2 = 8;        // IN2 pin for L293D
const int ledPin = 6;          // LED connected to digital pin 6
const int buzzerPin = 5;       // Buzzer connected to digital pin 5
const int turbidityPin = A1;   // Turbidity sensor connected to A1

// Temperature sensor setup
const int tempPin = 2;  // Pin connected to the DS18B20 data line
OneWire oneWire(tempPin);  // Create a OneWire instance
DallasTemperature sensors(&oneWire);  // Create a DallasTemperature instance

bool motorState = false;       // Tracks whether the motor is on or off
unsigned long previousMillis = 0; // Tracks the last state change
const long motorOnTime = 20000;  // Motor on for 20 seconds (in milliseconds)
const long motorOffTime = 5000;  // Motor off for 5 seconds

void setup() {
  Serial.begin(9600);  // Start serial communication
  pinMode(motorEnablePin, OUTPUT); // Motor enable pin
  pinMode(motorIn1, OUTPUT);       // Motor direction pin 1
  pinMode(motorIn2, OUTPUT);       // Motor direction pin 2
  pinMode(ledPin, OUTPUT);         // LED pin
  pinMode(buzzerPin, OUTPUT);      // Buzzer pin
  pinMode(turbidityPin, INPUT);    // Turbidity sensor pin

  // Set initial motor direction
  digitalWrite(motorIn1, HIGH);
  digitalWrite(motorIn2, LOW);

  // Ensure LED and buzzer are off initially
  digitalWrite(ledPin, LOW);
  digitalWrite(buzzerPin, LOW);

  sensors.begin();  // Start the temperature sensor
}

void loop() {
  unsigned long currentMillis = millis();

  // Read the temperature from the DS18B20 sensor
  sensors.requestTemperatures();  // Request temperature from sensor
  int temperature = sensors.getTempCByIndex(0);  // Get temperature in Celsius

  // Read turbidity from sensor
  int turbidityValue = analogRead(turbidityPin); // Read analog value
  int turbidityVoltage = turbidityValue * (5.0 / 1023.0); // Convert to voltage


  // Motor ON condition: Runs for 20 seconds
  if (motorState && (currentMillis - previousMillis >= motorOnTime)) {
    previousMillis = currentMillis;  // Update the time
    motorState = false;  // Turn off motor after 20 seconds
  }

  // Motor OFF condition: Runs for 5 seconds
  if (!motorState && (currentMillis - previousMillis >= motorOffTime)) {
    previousMillis = currentMillis;  // Update the time
    motorState = true;  // Turn motor on after 5 seconds
  }

  // Control motor and notification
  if (motorState) {
    // Turn the motor on
    analogWrite(motorEnablePin, 255);

    // Notify with LED and buzzer
    digitalWrite(ledPin, HIGH);
    digitalWrite(buzzerPin, HIGH);
    delay(200); // Buzzer and LED on for 200 ms
    digitalWrite(buzzerPin, LOW); // Turn off buzzer after notification

    // Send motor and buzzer state to Serial Monitor
    
  // Display temperature and turbidity data
  char output [100];
  sprintf(output, "Motor: ON, Temperature: %.d, Turbidity: %d, Voltage: %d", temperature, turbidityValue, turbidityVoltage);
  Serial.println(output);
  } else {
    // Turn the motor off
    analogWrite(motorEnablePin, 0);

    // Turn off LED
    digitalWrite(ledPin, LOW);

    // Send motor state to Serial Monitor
    char output [100];
    sprintf(output, "Motor: OFF, Temperature: %.d, Turbidity: %d, Voltage: %d", temperature, turbidityValue, turbidityVoltage);
    Serial.println(output);
  }

  delay(1000); // Small delay before the next loop iteration
}