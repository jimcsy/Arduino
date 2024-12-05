package main;

import javax.swing.*;
import java.awt.*;

public class UserInterface {
    // Method to create the top section (two boxes side by side)
    public static JPanel createTopSection() {
        JPanel topSection = new JPanel();
        topSection.setLayout(new GridLayout(1, 2, 0, 0)); // 1 row, 2 columns, 10px gap
        topSection.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add left box and right box
        topSection.add(createLeftBox());
        topSection.add(createRightBox());

        return topSection;
    }

    // Method to create the left box in the top section
    public static JPanel createLeftBox() { //value
        JPanel leftBox = new JPanel(new GridLayout(3, 1)); // 3 rows, 1 column

        JLabel leftLabel = new JLabel("Turbidity", SwingConstants.CENTER);
        leftLabel.setFont(new Font("Helvetica", Font.BOLD, 30));

        JLabel leftValue = new JLabel("Value", SwingConstants.CENTER);
        leftValue.setFont(new Font("Helvetica", Font.PLAIN, 24));

        JLabel leftData = new JLabel("Highest", SwingConstants.CENTER);
        leftData.setFont(new Font("Helvetica", Font.PLAIN, 15));

        leftBox.add(leftLabel);
        leftBox.add(leftValue);
        leftBox.add(leftData);
        leftBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return leftBox;
    }

    // Method to create the right box in the top section
    public static JPanel createRightBox() {
        JPanel rightBox = new JPanel(new GridLayout(3, 1)); // 3 rows, 1 column

        JLabel rightLabel = new JLabel("Temperature", SwingConstants.CENTER);
        rightLabel.setFont(new Font("Helvetica", Font.BOLD, 30));

        JLabel rightValue = new JLabel("Value", SwingConstants.CENTER);
        rightValue.setFont(new Font("Helvetica", Font.PLAIN, 24));

        JLabel rightData = new JLabel("Value", SwingConstants.CENTER);
        rightData.setFont(new Font("Helvetica", Font.PLAIN, 15));

        rightBox.add(rightLabel);
        rightBox.add(rightValue);
        rightBox.add(rightData);
        rightBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return rightBox;
    }

    // Method to create the bottom section (two boxes side by side)
    public static JPanel createBottomSection(String power) {
        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new GridLayout(1, 2, 0, 0)); // 50px horizontal gap
        bottomSection.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add left and right bottom boxes
        bottomSection.add(createBottomLeftBox());
        bottomSection.add(createBottomRightBox(power));

        return bottomSection;
    }

    // Method to create the bottom-left box
    public static JPanel createBottomLeftBox() {
        JPanel bottomLeftBox = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column

        JLabel bottomLeftLabel = new JLabel("Fan Speed", SwingConstants.CENTER);
        bottomLeftLabel.setFont(new Font("Helvetica", Font.BOLD, 30));

        JLabel bottomLeftValue = new JLabel("30%", SwingConstants.CENTER);
        bottomLeftValue.setFont(new Font("Helvetica", Font.PLAIN, 30));

        bottomLeftBox.add(bottomLeftLabel);
        bottomLeftBox.add(bottomLeftValue);
        bottomLeftBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return bottomLeftBox;
    }

    // Method to create the bottom-right box
    public static JPanel createBottomRightBox(String power) {
        JPanel bottomRightBox = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column

        JLabel bottomRightLabel = new JLabel( power, SwingConstants.CENTER);
        bottomRightLabel.setFont(new Font("Helvetica", Font.BOLD, 30));

        bottomRightBox.add(bottomRightLabel);
        bottomRightBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return bottomRightBox;
    }
}
