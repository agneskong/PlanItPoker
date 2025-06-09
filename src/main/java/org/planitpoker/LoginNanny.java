package org.planitpoker;

import org.planitpoker.Logger;

public class LoginNanny {

    private Main main;

    public LoginNanny(Main main) {
        this.main = main;
    }

    public void enterRoom(String name) {
        Logger.getLogger().info(name + " Entering a room...");
        login(name);
        switchGUI();
    }

    public void login(String name) {
        Logger.getLogger().info(name + " Logging in...");
        Blackboard.addName(name);
        String room = Blackboard.getCurrentRoom();
        if (room != null) {
            joinRoom(name, room);
        }
        switchGUI();
    }

    public void joinRoom(String name, String room) {
        try {
            MQTTPublisher publisher = new MQTTPublisher();
            String msg = "join-room:" + room + ":" + name;
            publisher.publish("planitpoker/events", msg);
    
            // Request stories
            String requestMsg = String.format("request-stories:%s:%s", room, name);
            publisher.publish("planitpoker/events", requestMsg);
    
            // Request users
            String requestUsers = String.format("request-users:%s:%s", room, name);
            publisher.publish("planitpoker/events", requestUsers);
    
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        Logger.getLogger().info("Logging out...");
        if (!Blackboard.getNames().isEmpty()) {
            Blackboard.getNames().removeLast();
        }
        main.setTitle("Login");
        LoginNanny loginNanny = new LoginNanny(main);
        LoginPanel loginPanel = new LoginPanel(loginNanny);
        main.setContentPane(loginPanel);
        main.setSize(400, 400);
        main.revalidate();
        main.repaint();
    }

    private void switchGUI() {
        main.setTitle("Room");
        CreateRoomNanny createRoomNanny = new CreateRoomNanny(main, this);
        CreateRoomPanel createRoomPanel = new CreateRoomPanel(createRoomNanny, this);
        main.setContentPane(createRoomPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }
}
