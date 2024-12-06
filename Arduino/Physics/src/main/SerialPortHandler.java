package main;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class SerialPortHandler{
    ArduinoSerialReader connect = new ArduinoSerialReader();
    private String temp;
    private String turbidity;
    private String speed;
    private String power;
    SerialPort comPort;

    public String getPower() {
        //System.out.println("Motor: " + power);
        return power;
    }

    public String getTemp() {
        //System.out.println("Temperature: " + temp);
        return temp;
    }

    public String getTurbidity() {
        //System.out.println("Turbidity: " + turbidity);
        return turbidity;
    }

    public String getSpeed() {
        //System.out.println("Speed: " + speed);
        return speed;
    }

    public boolean connect() {
        // Find available ports
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            System.out.println("No serial ports available.");
            return false;
        }

        // Select the first available port (for example)
        comPort = ports[0];
        System.out.println("Selected Port: " + comPort.getSystemPortName());

        // Set port parameters: 9600 baud, 8 data bits, 1 stop bit, no parity
        comPort.setComPortParameters(9600, 8, 1, 0);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        // Open the port
        if (comPort.openPort()) {
            System.out.println("Port opened successfully.");
            return true;
        } else {
            System.out.println("Failed to open port. Error: " + comPort.getPortDescription());
            return false;
        }
    }

    // Separate method to handle reading from the port
    public void readFromPort() {
        int n = 1;
        try {
            if (!comPort.isOpen()) {
                System.out.println("Port is not open.");
                return;
            }

            // Read data if available
            Scanner scanner = new Scanner(comPort.getInputStream());
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line + n++);
                processData(line);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Handle read exceptions
        }
    }

    // Method to close the port when done
    public void closePort() {
        if (comPort != null && comPort.isOpen()) {
            comPort.closePort();
            System.out.println("Port closed.");
        }
    }

    private String[] spliceData(String line) {
        // Split the line by ", " to get individual key-value pairs
        String[] parts = line.split(", ");
        
        // Initialize an array to hold the final key-value pairs
        String[] keyValuePairs = new String[parts.length * 2];  // Each part will have a key and a value

        int index = 0;
        // Iterate over each part and split by ": " to separate the key and the value
        for (String part : parts) {
            String[] keyValue = part.split(": ", 2);
            keyValuePairs[index++] = keyValue[0];  // Add the key
            keyValuePairs[index++] = keyValue[1];  // Add the value
        }

        return keyValuePairs;  // Return the array containing all keys and values
    }

    private void processData(String line){
        // Call spliceData method
        String[] result = spliceData(line);
        
        this.power = result[1];
        this.temp = result[3];
        this.turbidity = result[5];
        this.speed = result[7];
    }

    // Separate method to process the incoming data
    /*private void processData(String line) {
        if (line.contains("Motor: ")) {
            String[] powerValue = spliceData(line);
            if (powerValue.length > 1) {
                this.power = powerValue[1];
            } else {
                System.out.println("Invalid data format.");
            }
        } 
        
        if (line.contains("Temperature: ")) {
            String[] tempValue = spliceData(line);
            if (tempValue.length > 1) {
                this.temp = tempValue[1];
            } else {
                System.out.println("Invalid data format.");
            }
        } 
        
        if (line.contains("Turbidity: ")) {
            String[] turbidityValue = spliceData(line);
            if (turbidityValue.length > 1) {
                this.turbidity = turbidityValue[1];
            } else {
                System.out.println("Invalid data format.");
            }
        } 
        
        if (line.contains("Voltage: ")) {
            String[] speedValue = spliceData(line);
            if (speedValue.length > 1) {
                this.speed = speedValue[1];
            } else {
                System.out.println("Invalid data format.");
            }
        }
    }*/
}
