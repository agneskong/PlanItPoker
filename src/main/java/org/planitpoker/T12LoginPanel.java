package org.planitpoker;

import javax.swing.*;
import java.awt.*;

/**
 * The LoginPanel class provides the initial user interface for entering or logging into a Planning Poker room.
 * It features input fields and buttons for name entry and handles interaction through the LoginNanny controller.
 *
 * Core responsibilities include:
 * - Displaying a user-friendly layout for entering the application.
 * - Triggering the login or room entry process via action listeners.
 * - Visually guiding the user with labels and input cues.
 *
 * Author: Sathvik Chilakala
 * Date: June 12, 2025
 */


public class T12LoginPanel extends JPanel {

    public T12LoginPanel(T12LoginNanny joinRoomNanny) {
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
        add(Box.createVerticalStrut(4));
        add(loginButton);

        enterButton.addActionListener(e -> joinRoomNanny.enterRoom(nameField.getText()));
        loginButton.addActionListener(e -> joinRoomNanny.login(nameField.getText()));
    }
}
