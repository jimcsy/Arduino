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
        return power;
    }

    public String getTemp() {
        return temp;
    }

    public String getTurbidity() {
        System.out.println(turbidity);
        return turbidity;
    }

    public String getSpeed() {
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
            // Reading from the port
        } else {
            System.out.println("Failed to open port. Error: " + comPort.getPortDescription());
            return false;
        }
    }

    // Separate method to handle reading from the port
    public void readFromPort() {
        try (Scanner scanner = new Scanner(comPort.getInputStream())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                processData(line);
                connect.show();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle read exceptions
        } finally {
            // Close the port in the finally block to ensure it's always closed
            if (comPort.isOpen()) {
                try {
                    comPort.closePort();
                    System.out.println("Port closed.");
                    Thread.sleep(1000); // Allow the system to release the port
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String[] spliceData(String line) {
        // Check if the line contains ": "
        if (line.contains(": ")) {
            return line.split(": ", 2); // The second parameter makes sure we split into only two parts
        } else {
            // If no ": " is present, return the original line as a single part
            return new String[] { line };
        }
    }

    // Separate method to process the incoming data
    private void processData(String line) {
        if (line.contains("Motor: ")) {
            String [] powerValue = spliceData(line);
            if (powerValue.length > 1) {
                this.power = powerValue[1];
            } else {
                System.out.println("Invalid data format.");
            }
        }else if (line.contains("Temperature: ")){
            String [] tempValue = spliceData(line);
            if (tempValue.length > 1) {
                this.temp = tempValue[1];
            } else {
                System.out.println("Invalid data format.");
            }
        }else if (line.contains("Turbidity Raw Value: ")){
            String [] turbidityValue = spliceData(line);
            if (turbidityValue.length > 1) {
                this.turbidity = turbidityValue[1];
            } else {
                System.out.println("Invalid data format.");
            }
        }else if (line.contains ("Voltage: ")){
            String [] speedValue = spliceData(line);
            if (speedValue.length > 1) {
                this.speed = speedValue[1];
            } else {
                System.out.println("Invalid data format.");
            }
        }
    }
}
