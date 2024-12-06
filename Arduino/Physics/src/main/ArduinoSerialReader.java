package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class ArduinoSerialReader {

    private double rotationAngle = 4;

    public void createAndShowGUI() {
        JFrame frame;
        JLabel leftValue;
        JLabel rightValue;
        JLabel bottomLeftValue;
        JLabel bottomRightLabel;
        SerialPortHandler values;

        frame = new JFrame("AGAPAY Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        Color backgroundColor = new Color(30, 30, 30);
        Color panelColor = new Color(45, 45, 45);
        Color textColor = new Color(200, 200, 200);
        Color accentColor = new Color(50, 205, 50);

        JPanel topSection = new JPanel(new GridLayout(1, 2, 20, 20));
        topSection.setBackground(backgroundColor);

        JPanel leftBox = createInfoPanel("Turbidity", textColor, panelColor, accentColor);
        leftValue = (JLabel) leftBox.getComponent(1);

        JPanel rightBox = createInfoPanel("Temperature", textColor, panelColor, accentColor);
        rightValue = (JLabel) rightBox.getComponent(1);

        topSection.add(leftBox);
        topSection.add(rightBox);

        JPanel bottomSection = new JPanel(new GridBagLayout());
        bottomSection.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        JPanel bottomLeftBox = new JPanel();
        bottomLeftBox.setLayout(new BoxLayout(bottomLeftBox, BoxLayout.Y_AXIS));
        bottomLeftBox.setBackground(panelColor);
        bottomLeftBox.setBorder(BorderFactory.createLineBorder(accentColor, 2, true));

        JLabel fanLabel = new JLabel("Fan Speed", SwingConstants.CENTER);
        fanLabel.setFont(new Font("Arial", Font.BOLD, 24));
        fanLabel.setForeground(textColor);
        fanLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomLeftBox.add(fanLabel);

        // Add the rotating fan panel
        RotatingFanPanel fanPanel = new RotatingFanPanel(panelColor, accentColor, textColor);
        bottomLeftBox.add(fanPanel);

        // Value label below the fan panel, always displayed
        bottomLeftValue = new JLabel("0", SwingConstants.CENTER); // Default value "0"
        bottomLeftValue.setFont(new Font("Arial", Font.BOLD, 50));
        bottomLeftValue.setForeground(textColor);
        bottomLeftBox.add(bottomLeftValue);
        

        bottomSection.add(bottomLeftBox, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;

        JPanel bottomRightBox = new JPanel(new BorderLayout());
        bottomRightBox.setBackground(panelColor);
        bottomRightBox.setBorder(BorderFactory.createLineBorder(accentColor, 2, true));
        bottomRightLabel = new JLabel("OFF", SwingConstants.CENTER);
        bottomRightLabel.setFont(new Font("Arial", Font.BOLD, 30));
        bottomRightLabel.setForeground(textColor);
        bottomRightLabel.setOpaque(true);
        bottomRightLabel.setBackground(Color.RED); 
        bottomRightBox.add(bottomRightLabel, BorderLayout.CENTER);

        bottomSection.add(bottomRightBox, gbc);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(topSection);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(bottomSection);

        frame.add(mainPanel);
        frame.setVisible(true);

        values = new SerialPortHandler();
        if (values.connect()) {
            frame.setVisible(true);

            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    values.readFromPort();
                    String newTurbidity = values.getTurbidity();
                    String newTemperature = values.getTemp();
                    String newFanSpeed = values.getSpeed();
                    String power = values.getPower();

                    leftValue.setText(newTurbidity);
                    rightValue.setText(newTemperature + " C");

                    // Update fan speed and status regardless of power state
                    if (power.equals("ON")) {
                        bottomRightLabel.setBackground(accentColor);
                        bottomRightLabel.setText("ON");
                        double speed = Double.parseDouble(newFanSpeed);
                        bottomLeftValue.setText(newFanSpeed); // Update fan speed value
                        fanPanel.setFanSpeed(speed);
                    } else {
                        bottomRightLabel.setBackground(Color.RED);
                        bottomRightLabel.setText("OFF");
                        bottomLeftValue.setText("0"); // Keep showing the last fan speed
                        fanPanel.setFanSpeed(0); // Stop the fan animation, but keep value
                    }
                }
            });

            timer.start();
        }
    }

    private JPanel createInfoPanel(String title, Color textColor, Color panelColor, Color borderColor) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColor);

        JLabel valueLabel = new JLabel("N/A", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 50));
        valueLabel.setForeground(textColor);

        panel.add(titleLabel);
        panel.add(valueLabel);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArduinoSerialReader().createAndShowGUI());
    }

    class RotatingFanPanel extends JPanel {
        private double fanSpeed = 0;

        public RotatingFanPanel(Color panelColor, Color borderColor, Color textColor) {
            setBackground(panelColor);
        }

        public void setFanSpeed(double speed) {
            this.fanSpeed = speed;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(getWidth(), getHeight()) / 4;

            g2d.setColor(Color.WHITE);
            AffineTransform old = g2d.getTransform();

            // Update the rotation angle based on fan speed
            rotationAngle += fanSpeed * 0.3;

            g2d.rotate(rotationAngle, centerX, centerY);

            // Set the stroke to make the lines thicker
            g2d.setStroke(new BasicStroke(4)); // Set the stroke width to 4 pixels (adjust as needed)

            // Draw the 4 grid lines (fan blades) indicating fan speed
            for (int i = 0; i < 4; i++) {
                double angle = Math.toRadians(i * 90);
                int startX = centerX + (int) (Math.cos(angle) * radius);
                int startY = centerY + (int) (Math.sin(angle) * radius);
                int endX = centerX + (int) (Math.cos(angle) * (radius + 50));
                int endY = centerY + (int) (Math.sin(angle) * (radius + 50));

                g2d.drawLine(startX, startY, endX, endY); 
            }

            g2d.setTransform(old);
        }
    }
}
