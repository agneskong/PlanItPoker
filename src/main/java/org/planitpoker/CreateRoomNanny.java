package org.planitpoker;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Act as a controller for the CreateRoomPanel.
 *
 * @author javiergs
 * @author agneskong
 */
public class CreateRoomNanny {

    private Main main;

    public CreateRoomNanny(Main main) {
        this.main = main;
    }

    public void createRoom(String name, String selectedItem) {
        System.out.println(" Creating room..." + name + ", mode: " + selectedItem);
        Blackboard.addCurrentRoom(name);
        Blackboard.addCurrentMode(selectedItem);
        switchGUI();
    }

    private void switchGUI() {
        main.setTitle("Stories");
        StoriesNanny createRoomNanny = new StoriesNanny(main);
        StoriesPanel createRoomPanel = new StoriesPanel(createRoomNanny);
        main.setContentPane(createRoomPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }
    public void publishRoomCreation(String roomName) {
        try {
            MQTTPublisher publisher = new MQTTPublisher();
            publisher.publish("planit/create", roomName);
            publisher.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}