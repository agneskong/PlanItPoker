package org.planitpoker;

/**
 * The LoginNanny class manages the login and room entry logic for the PlanitPoker application.
 * It handles user authentication, MQTT room communication, and dynamic GUI transitions.
 *
 * Core responsibilities include:
 * - Logging users in and out.
 * - Publishing MQTT messages to join a room and request room data (users and stories).
 * - Updating the GUI between login and main panels based on user status.
 *
 * This class acts as a controller between user interaction and backend communication.
 *
 * Author: Sathvik Chilakala and Justin Diaz
 * Date: June 12, 2025
 */


public class T12LoginNanny {
    private T12Main main;

    public T12LoginNanny(T12Main main) {
        this.main = main;
    }

    public void enterRoom(String name) {
        T12Logger.getLogger().info(name + " Entering a room...");
        login(name);
    }

    public void login(String name) {
        T12Logger.getLogger().info(name + " Logging in...");
        T12Blackboard.addName(name);
        String room = T12Blackboard.getCurrentRoom();
        if (room != null) {
            joinRoom(name, room);
        }
        switchGUI();
    }

    public void joinRoom(String name, String room) {
        try {
            T12MQTTPublisher publisher = new T12MQTTPublisher();
            publisher.publish("planitpoker/events", "join-room:" + name + ":" + room);
            publisher.publish("planitpoker/events", "request-stories:" + room + ":" + name);
            publisher.publish("planitpoker/events", "request-users:" + room + ":" + name);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        T12Logger.getLogger().info("Logging out...");
        if (!T12Blackboard.getNames().isEmpty()) {
            T12Blackboard.getNames().removeLast();
        }
        main.setTitle("Login");
        T12LoginPanel loginPanel = new T12LoginPanel(new T12LoginNanny(main));
        main.setContentPane(loginPanel);
        main.setSize(400, 400);
        main.revalidate();
        main.repaint();
    }

    private void switchGUI() {
        main.setTitle("Room");
        T12CreateRoomPanel createRoomPanel = new T12CreateRoomPanel(new T12CreateRoomNanny(main, this), this);
        main.setContentPane(createRoomPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }
}

