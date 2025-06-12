package org.planitpoker;

/**
 * The  class manages the creation of new Planning Poker rooms.
 * It sets the current room and mode in the shared, publishes
 * the creation event via MQTT if it's a new room, and updates the GUI accordingly.
 * This class acts as a controller between the login flow and room setup.
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public class T12CreateRoomNanny {

    private T12Main main;
    private T12LoginNanny loginNanny;

    public T12CreateRoomNanny(T12Main main, T12LoginNanny loginNanny) {
        this.main = main;
        this.loginNanny = loginNanny;
    }

    public void createRoom(String name, String selectedItem) {
        T12Logger.getLogger().info("Creating room: " + name + ", mode: " + selectedItem);
        boolean isNewRoom = T12Blackboard.getCurrentRoom() == null || !T12Blackboard.getCurrentRoom().equals(name);

        T12Blackboard.addCurrentRoom(name);
        T12Blackboard.addCurrentMode(selectedItem);

        if (isNewRoom) {
            publishRoomCreation(name);
        }

        loginNanny.joinRoom(T12Blackboard.getNames().getLast(), name); // Re-send join logic
        switchGUI();
    }


    private void switchGUI() {
        main.setTitle("Stories");
        T12StoriesPanel storiesPanel = new T12StoriesPanel(new T12StoriesNanny(main, loginNanny));
        main.setContentPane(storiesPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }

    public void publishRoomCreation(String roomName) {
        try {
            T12MQTTPublisher publisher = new T12MQTTPublisher();
            publisher.publish("planitpoker/events", "create-room:" + roomName);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
