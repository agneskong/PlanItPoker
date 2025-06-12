package org.planitpoker;

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
