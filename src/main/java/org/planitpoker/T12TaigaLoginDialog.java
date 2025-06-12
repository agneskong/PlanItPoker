package org.planitpoker;

import javax.swing.*;
import java.awt.*;

/**
 * TaigaLoginDialog is a modal dialog that prompts the user to enter their
 * Taiga username and password for authentication.
 *
 * It provides simple text fields for username and password input, and a login button.
 * Once the login button is pressed, the dialog closes and indicates submission.
 *
 * Author: Agnes Kong
 * Date: June 12, 2025
 */

public class T12TaigaLoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean submitted = false;

    public T12TaigaLoginDialog(JFrame parent) {
        super(parent, "Taiga Login", true);
        setSize(300, 150);
        setLayout(new GridLayout(3, 1));

        usernameField = new JTextField("Username");
        passwordField = new JPasswordField("Password");

        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        add(usernameField);
        add(passwordField);
        add(loginBtn);
    }

    public boolean wasSubmitted() {
        return submitted;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}
