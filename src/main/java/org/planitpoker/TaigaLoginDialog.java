package org.planitpoker;

import javax.swing.*;
import java.awt.*;

public class TaigaLoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean submitted = false;

    public TaigaLoginDialog(JFrame parent) {
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
