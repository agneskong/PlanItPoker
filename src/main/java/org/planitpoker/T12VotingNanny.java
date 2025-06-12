package org.planitpoker;

/**
 * Controller responsible for managing voting interactions in the Planning Poker app.
 *
 * This class handles sending estimates, revealing cards, and broadcasting results
 * by publishing relevant MQTT messages to the designated topic.
 * It also manages GUI switching related to voting.
 *
 * Author: Agnes Kong
 * Date: June 12, 2025
 */

public class T12VotingNanny {
    private T12Main main;

    public T12VotingNanny(T12Main main) {
        this.main = main;
    }

    public void sendEstimate(String room, String story, String user, int estimate) {
        try {
            T12MQTTPublisher publisher = new T12MQTTPublisher();
            String msg = String.format("estimate:%s:%s:%s:%d", room, story, user, estimate);
            publisher.publish("planitpoker/events", msg);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void revealCards(String room, String story) {
        try {
            T12MQTTPublisher publisher = new T12MQTTPublisher();
            String user = T12Blackboard.getNames().isEmpty() ? "" : T12Blackboard.getNames().getLast();
            String msg = String.format("reveal:%s:%s:%s", room, story, user);
            publisher.publish("planitpoker/events", msg);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcastResult(String room, String story, double avg) {
        try {
            T12MQTTPublisher publisher = new T12MQTTPublisher();
            String user = T12Blackboard.getNames().isEmpty() ? "" : T12Blackboard.getNames().getLast();
            String msg = String.format("result:%s:%s:%.2f:%s", room, story, avg, user);
            publisher.publish("planitpoker/events", msg);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchGUI() {
        main.setTitle("Room");
        // Updated for new constructors:
        T12CreateRoomNanny createRoomNanny = new T12CreateRoomNanny(main, main.getLoginNanny());
        T12CreateRoomPanel createRoomPanel = new T12CreateRoomPanel(createRoomNanny, main.getLoginNanny());
        main.setContentPane(createRoomPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }
}
