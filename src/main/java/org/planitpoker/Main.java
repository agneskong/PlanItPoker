package org.planitpoker;

import javax.swing.*;
import org.planitpoker.Logger;
import org.planitpoker.ConsoleAppender;
import org.planitpoker.FileAppender;

public class Main extends JFrame {
    private LoginNanny loginNanny;

    public Main() {
        // Initialize loggers
        Logger.addAppender(new ConsoleAppender());
        Logger.addAppender(new FileAppender());
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
