package org.planitpoker;

import org.json.JSONObject;

/**
 * Controller responsible for managing the voting and its interactions.
 *
 * @author agneskong
 */
public class VotingNanny {
    private Main main;

    public VotingNanny(Main main) {
        this.main = main;
    }

    public void sendEstimate(String room, String story, String user, int estimate) {
        try {
            MQTTPublisher publisher = new MQTTPublisher();
            String msg = String.format("estimate:%s:%s:%s:%d", room, story, user, estimate);
            publisher.publish("planitpoker/events", msg);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void revealCards(String room, String story) {
        try {
            MQTTPublisher publisher = new MQTTPublisher();
            String msg = String.format("reveal:%s:%s", room, story);
            publisher.publish("planitpoker/events", msg);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcastResult(String room, String story, double avg) {
        try {
            MQTTPublisher publisher = new MQTTPublisher();
            String msg = String.format("result:%s:%s:%.2f", room, story, avg);
            publisher.publish("planitpoker/events", msg);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchGUI() {
        main.setTitle("Room");
        // Updated for new constructors:
        CreateRoomNanny createRoomNanny = new CreateRoomNanny(main, main.getLoginNanny());
        CreateRoomPanel createRoomPanel = new CreateRoomPanel(createRoomNanny, main.getLoginNanny());
        main.setContentPane(createRoomPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }
}
