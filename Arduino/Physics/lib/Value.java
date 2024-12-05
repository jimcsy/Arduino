package main;

import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;

public class Value {
    private String temp;
    private String turbidity;
    private String speed;
    private String power;

    public String getPower() {
        return power;
    }

    public void connect() {
        // Find and open the serial port
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length == 0) {
            System.out.println("No serial ports available.");
            return;
        }

        // List available ports
        for (SerialPort port : ports) {
            System.out.println("Available Port: " + port.getSystemPortName());
        }

        // Use a specific port or ask the user to choose
        SerialPort comPort = ports[0]; // Adjust if necessary, or implement logic for user selection

        // Configure the port parameters (baud rate, data bits, stop bits, parity)
        comPort.setComPortParameters(9600, 8, 1, 0); // Match the settings in Arduino
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        // Open the port
        if (comPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.out.println("Failed to open port. Error: " + comPort.getPortDescription());
            return;
        }

        // Read data from Arduino
        try (Scanner scanner = new Scanner(comPort.getInputStream())) {
            while (true) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println("Arduino says: " + line);
                    // Check if the line contains "Motor: "
                    if (line.contains("Motor: ")) {
                        String[] parts = line.split(": ");
                        if (parts.length > 1) {
                            this.power = parts[1];
                            System.out.println("Power: " + this.power);
                        } else {
                            System.out.println("Invalid line format: Missing value after 'Motor:'");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (comPort.isOpen()) {
                try {
                    comPort.closePort();
                    System.out.println("Port closed.");
                    Thread.sleep(500); // Allow system to release the port
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
