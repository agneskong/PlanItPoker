package org.planitpoker;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    public LoginPanel(LoginNanny joinRoomNanny) {
        setBackground(new Color(245, 248, 255));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Let's start!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Join the room:", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField("Enter your name");
        nameField.setMaximumSize(new Dimension(240, 32));
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel accountLabel = new JLabel("Already have an account?");
        accountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(32));
        add(titleLabel);
        add(Box.createVerticalStrut(16));
        add(subtitleLabel);
        add(Box.createVerticalStrut(8));
        add(nameField);
        add(Box.createVerticalStrut(8));
        add(enterButton);
        add(Box.createVerticalStrut(18));
        add(accountLabel);
        add(Box.createVerticalStrut(6));
        add(loginButton);

        enterButton.addActionListener(e -> joinRoomNanny.enterRoom(nameField.getText()));
        loginButton.addActionListener(e -> joinRoomNanny.login(nameField.getText()));
    }
}
