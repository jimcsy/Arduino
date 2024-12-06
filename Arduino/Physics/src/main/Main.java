package main;

public class Main {
    public static void main(String[] args) {
        // Initialize the ArduinoSerialReader object and start the GUI
        ArduinoSerialReader arduinoSerialReader = new ArduinoSerialReader();
        arduinoSerialReader.createAndShowGUI();
    }
}
