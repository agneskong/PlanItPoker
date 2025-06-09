package org.planitpoker;

public class CreateRoomNanny {

    private Main main;
    private LoginNanny loginNanny;

    public CreateRoomNanny(Main main, LoginNanny loginNanny) {
        this.main = main;
        this.loginNanny = loginNanny;
    }

    public void createRoom(String name, String selectedItem) {
        System.out.println(" Creating room..." + name + ", mode: " + selectedItem);
        Blackboard.addCurrentRoom(name);
        Blackboard.addCurrentMode(selectedItem);
        switchGUI();
    }

    private void switchGUI() {
        main.setTitle("Stories");
        StoriesNanny storiesNanny = new StoriesNanny(main, loginNanny);
        StoriesPanel storiesPanel = new StoriesPanel(storiesNanny);
        main.setContentPane(storiesPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }

    public void publishRoomCreation(String roomName) {
        try {
            MQTTPublisher publisher = new MQTTPublisher();
            String msg = "create-room:" + roomName;
            publisher.publish("planitpoker/events", msg);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
