package org.planitpoker;

import javax.swing.*;
import java.awt.*;

/**
 * The Main class serves as the entry point and primary JFrame container for the Planning Poker application.
 * It initializes the logging system and distributed event handler, then loads the initial login interface.
 *
 * Core responsibilities include:
 * - Bootstrapping the application and setting up shared infrastructure (logging, event handling).
 * - Hosting the login interface and providing access to the LoginNanny for authentication flow.
 * - Managing application lifecycle and GUI transitions.
 *
 * Author: Sathvik Chilakala
 * Date: June 12, 2025
 */


public class Main extends JFrame {
    private LoginNanny loginNanny;
    private static boolean loggerInitialized = false;
    private static DistributedEventHandler eventHandler;

    public Main() {
        initializeLoggerOnce();
        initializeEventHandlerOnce();

        loginNanny = new LoginNanny(this);
        LoginPanel loginPanel = new LoginPanel(loginNanny);
        setContentPane(loginPanel);
    }

    public LoginNanny getLoginNanny() {
        return loginNanny;
    }

    private void initializeLoggerOnce() {
        if (!loggerInitialized) {
            Logger.addAppender(new ConsoleAppender());
            Logger.addAppender(new FileAppender());
            loggerInitialized = true;
        }
    }

    private void initializeEventHandlerOnce() {
        if (eventHandler == null) {
            try {
                eventHandler = new DistributedEventHandler("planitpoker/events", this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Optional: Reset static state to ensure clean rerun
            Blackboard.logoutCurrentUser();  // Clears last user
            Frame[] oldFrames =  JFrame.getFrames();
            for (Frame f : oldFrames) f.dispose();

            Main main = new Main();
            main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            main.setSize(400, 400);
            main.setLocationRelativeTo(null);
            main.setVisible(true);
        });
    }
}
