package main;

import javax.swing.*;
import java.awt.*;

public class ArduinoSerialReader {
    // Declare instance variables for the frame and labels
    public JFrame frame;
    public JLabel leftValue;
    public JLabel rightValue;
    public JLabel bottomLeftValue;
    public JLabel bottomRightLabel;
    public SerialPortHandler values;

    public void createAndShowGUI() {
        // Create the main frame for the GUI
        frame = new JFrame("AGAPAY");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        // Top section (two boxes side by side)
        JPanel topSection = new JPanel();
        topSection.setLayout(new GridLayout(1, 2, 0, 0)); // 1 row, 2 columns, 10px gap
        topSection.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Create the left box panel with two labels
        JPanel leftBox = new JPanel(new GridLayout(3, 1));  // 3 rows, 1 column
        JLabel leftLabel = new JLabel("Turbidity", SwingConstants.CENTER);
        leftLabel.setFont(new Font("Helvetica", Font.BOLD, 30));
        leftValue = new JLabel("", SwingConstants.CENTER);
        leftValue.setFont(new Font("Helvetica", Font.PLAIN, 24));
        JLabel leftData = new JLabel("Highest", SwingConstants.CENTER);
        leftData.setFont(new Font("Helvetica", Font.PLAIN, 15));
        leftBox.add(leftLabel);
        leftBox.add(leftValue);
        leftBox.add(leftData);
        leftBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Create the right box panel with two labels
        JPanel rightBox = new JPanel(new GridLayout(3, 1));  // 3 rows, 1 column
        JLabel rightLabel = new JLabel("Temperature", SwingConstants.CENTER);
        rightLabel.setFont(new Font("Helvetica", Font.BOLD, 30));
        rightValue = new JLabel("", SwingConstants.CENTER);
        rightValue.setFont(new Font("Helvetica", Font.PLAIN, 24));
        JLabel rightData = new JLabel("Value", SwingConstants.CENTER);
        rightData.setFont(new Font("Helvetica", Font.PLAIN, 15));
        rightBox.add(rightLabel);
        rightBox.add(rightValue);
        rightBox.add(rightData);
        rightBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        topSection.add(leftBox);
        topSection.add(rightBox);

        // Bottom section (two boxes side by side)
        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new GridLayout(1, 2, 0, 0)); // 50px horizontal gap
        bottomSection.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel bottomLeftBox = new JPanel(new GridLayout(2, 1));
        JLabel bottomLeftLabel = new JLabel("Fan Speed", SwingConstants.CENTER);
        bottomLeftLabel.setFont(new Font("Helvetica", Font.BOLD, 30));
        bottomLeftValue = new JLabel("", SwingConstants.CENTER);
        bottomLeftValue.setFont(new Font("Helvetica", Font.PLAIN, 24));
        bottomLeftBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bottomLeftBox.add(bottomLeftLabel);
        bottomLeftBox.add(bottomLeftValue);

        // Right bottom box
        JPanel bottomRightBox = new JPanel(new GridLayout(1, 1));
        bottomRightLabel = new JLabel("", SwingConstants.CENTER);
        bottomRightLabel.setFont(new Font("Helvetica", Font.BOLD, 30));
        bottomRightBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bottomRightBox.add(bottomRightLabel);

        bottomSection.add(bottomLeftBox);
        bottomSection.add(bottomRightBox);

        // Main layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topSection);
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing between sections
        mainPanel.add(bottomSection);

        frame.add(mainPanel);

        // Create the SerialPortHandler object
        values = new SerialPortHandler();
        if(values.connect()){
            frame.setVisible(true);
            values.readFromPort(); //while
        } // This could throw an exception
        //show();
    }

    public void show() {
        /*Timer timer = new Timer(1001, new ActionListener() {
        //@Override
        public void actionPerformed(ActionEvent e) {*/
            // Simulate getting new data
            String newTurbidity = "hi";//values.getTurbidity();
            String newTemperature = "HI";//values.getTemp();
            String newFanSpeed = "HI" ;//values.getSpeed();
            String power = "ON";//values.getPower();

            // Update the labels with new values
            leftValue.setText("Hello");
            rightValue.setText("sasasq1");
            
            bottomRightLabel.setOpaque(true); // Makes sure the background color is visible
            if (power.equals("ON")) {
                bottomRightLabel.setBackground(Color.GREEN);
                bottomRightLabel.setText("ON");
                bottomLeftValue.setText(newFanSpeed);
            } else {
                bottomRightLabel.setText("OFF");
                bottomLeftValue.setText("");
                bottomRightLabel.setBackground(Color.RED);
            }
        }
        //);

        //Start the timer
        //timer.start();
    }
//}
    
