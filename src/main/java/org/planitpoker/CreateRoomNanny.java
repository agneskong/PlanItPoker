package org.planitpoker;

/**
 * The  class manages the creation of new Planning Poker rooms.
 * It sets the current room and mode in the shared {@link Blackboard}, publishes
 * the creation event via MQTT if it's a new room, and updates the GUI accordingly.
 * This class acts as a controller between the login flow and room setup.
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public class CreateRoomNanny {

    private Main main;
    private LoginNanny loginNanny;

    public CreateRoomNanny(Main main, LoginNanny loginNanny) {
        this.main = main;
        this.loginNanny = loginNanny;
    }

    public void createRoom(String name, String selectedItem) {
        Logger.getLogger().info("Creating room: " + name + ", mode: " + selectedItem);
        boolean isNewRoom = Blackboard.getCurrentRoom() == null || !Blackboard.getCurrentRoom().equals(name);

        Blackboard.addCurrentRoom(name);
        Blackboard.addCurrentMode(selectedItem);

        if (isNewRoom) {
            publishRoomCreation(name);
        }

        loginNanny.joinRoom(Blackboard.getNames().getLast(), name); // Re-send join logic
        switchGUI();
    }


    private void switchGUI() {
        main.setTitle("Stories");
        StoriesPanel storiesPanel = new StoriesPanel(new StoriesNanny(main, loginNanny));
        main.setContentPane(storiesPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }

    public void publishRoomCreation(String roomName) {
        try {
            MQTTPublisher publisher = new MQTTPublisher();
            publisher.publish("planitpoker/events", "create-room:" + roomName);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
