package org.planitpoker;

public class LoginNanny {
    private Main main;

    public LoginNanny(Main main) {
        this.main = main;
    }

    public void enterRoom(String name) {
        Logger.getLogger().info(name + " Entering a room...");
        login(name);
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
            publisher.publish("planitpoker/events", "join-room:" + name + ":" + room);
            publisher.publish("planitpoker/events", "request-stories:" + room + ":" + name);
            publisher.publish("planitpoker/events", "request-users:" + room + ":" + name);
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
        LoginPanel loginPanel = new LoginPanel(new LoginNanny(main));
        main.setContentPane(loginPanel);
        main.setSize(400, 400);
        main.revalidate();
        main.repaint();
    }

    private void switchGUI() {
        main.setTitle("Room");
        CreateRoomPanel createRoomPanel = new CreateRoomPanel(new CreateRoomNanny(main, this), this);
        main.setContentPane(createRoomPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }
}

