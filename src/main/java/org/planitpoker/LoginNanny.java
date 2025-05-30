package org.planitpoker;

/**
 * LoginNanny is responsible for handling the login process.
 *
 * @author javiergs
 */
public class LoginNanny {

    private Main main;

    public LoginNanny(Main main) {
        this.main = main;
    }

    public void enterRoom(String name) {
        System.out.println(name + " Entering a room...");
        login(name);
        // joinRoom(name, Blackboard.getCurrentRoom());
        switchGUI();
    }

    public void login(String name) {
        System.out.println(name + " Logging in...");
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
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchGUI() {
        main.setTitle("Room");
        CreateRoomNanny createRoomNanny = new CreateRoomNanny(main);
        CreateRoomPanel createRoomPanel = new CreateRoomPanel(createRoomNanny);
        main.setContentPane(createRoomPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }
}
