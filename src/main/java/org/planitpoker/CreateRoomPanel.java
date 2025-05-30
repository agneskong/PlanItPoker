package org.planitpoker;

import javax.swing.*;
import java.awt.*;

public class CreateRoomPanel extends JPanel {

    public CreateRoomPanel(CreateRoomNanny createRoomNanny) {
        setBackground(new Color(245, 248, 255));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create new Room", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel box1 = new JPanel();
        box1.setOpaque(false);
        box1.setLayout(new BoxLayout(box1, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField nameField = new JTextField("CSC307");
        nameField.setMaximumSize(new Dimension(200, 30));
        box1.add(nameLabel);
        box1.add(Box.createHorizontalStrut(10));
        box1.add(nameField);

        JPanel box2 = new JPanel();
        box2.setOpaque(false);
        box2.setLayout(new BoxLayout(box2, BoxLayout.X_AXIS));
        JLabel modeLabel = new JLabel("Mode:");
        modeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        String[] options = {"Scrum", "Fibonacci", "Sequential", "Hours", "T-shirt", "Custom deck"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setMaximumSize(new Dimension(180, 30));
        box2.add(modeLabel);
        box2.add(Box.createHorizontalStrut(10));
        box2.add(comboBox);

        JButton createButton = new JButton("Create");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(28));
        add(title);
        add(Box.createVerticalStrut(24));
        add(box1);
        add(Box.createVerticalStrut(18));
        add(box2);
        add(Box.createVerticalStrut(28));
        add(createButton);

        createButton.addActionListener(e ->
            createRoomNanny.createRoom(nameField.getText(), (String) comboBox.getSelectedItem())
        );
    }
}
