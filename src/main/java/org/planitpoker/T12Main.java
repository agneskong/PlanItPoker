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


public class T12Main extends JFrame {
    private T12LoginNanny loginNanny;
    private static boolean loggerInitialized = false;
    private static T12DistributedEventHandler eventHandler;

    public T12Main() {
        initializeLoggerOnce();
        initializeEventHandlerOnce();

        loginNanny = new T12LoginNanny(this);
        T12LoginPanel loginPanel = new T12LoginPanel(loginNanny);
        setContentPane(loginPanel);
    }

    public T12LoginNanny getLoginNanny() {
        return loginNanny;
    }

    private void initializeLoggerOnce() {
        if (!loggerInitialized) {
            T12Logger.addAppender(new T12ConsoleAppender());
            T12Logger.addAppender(new T12FileAppender());
            loggerInitialized = true;
        }
    }

    private void initializeEventHandlerOnce() {
        if (eventHandler == null) {
            try {
                eventHandler = new T12DistributedEventHandler("planitpoker/events", this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Optional: Reset static state to ensure clean rerun
            T12Blackboard.logoutCurrentUser();  // Clears last user
            Frame[] oldFrames =  JFrame.getFrames();
            for (Frame f : oldFrames) f.dispose();

            T12Main main = new T12Main();
            main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            main.setSize(400, 400);
            main.setLocationRelativeTo(null);
            main.setVisible(true);
        });
    }
}
