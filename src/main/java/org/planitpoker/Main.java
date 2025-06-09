package org.planitpoker;

import javax.swing.*;

public class Main extends JFrame {
    private LoginNanny loginNanny;

    public Main() {
        try {
            new DistributedEventHandler("planitpoker/events", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginNanny = new LoginNanny(this);
        LoginPanel loginPanel = new LoginPanel(loginNanny);
        add(loginPanel);
    }

    public LoginNanny getLoginNanny() {
        return loginNanny;
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(400, 400);
        main.setLocationRelativeTo(null);
        main.setVisible(true);
    }
}
