package org.planitpoker;

import org.eclipse.paho.client.mqttv3.*;

import javax.swing.*;

public class DistributedEventHandler implements MqttCallback {
    private MQTTSubscriber subscriber;

    public DistributedEventHandler(String topic) throws MqttException {
        subscriber = new MQTTSubscriber(topic, this);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("MQTT connection lost!");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        System.out.println("[MQTT RECEIVED] " + msg);

        // Message format: event:data
        if (msg.startsWith("create-room:")) {
            String room = msg.substring("create-room:".length());
            Blackboard.addCurrentRoom(room);
            // Optional: refresh UI
        } else if (msg.startsWith("join-room:")) {
            String[] parts = msg.split(":");
            String room = parts[1];
            String user = parts[2];
            Blackboard.addName(user);
            // Optional: refresh UI
        } else if (msg.startsWith("estimate:")) {
            // Format: estimate:room:story:user:vote
            String[] parts = msg.split(":");
            if (parts.length >= 5) {
                String room = parts[1];
                String storyTitle = parts[2];
                String user = parts[3];
                int vote = Integer.parseInt(parts[4]);
                for (Story story : Blackboard.getStories()) {
                    if (story.getTitle().equals(storyTitle)) {
                        story.submitVotes(user, vote);
                        break;
                    }
                }
                // Optional: refresh UI
            }
        } else if (msg.startsWith("reveal:")) {
            // Format: reveal:room:story
            String[] parts = msg.split(":");
            String storyTitle = parts[2];
            // Optional: Show votes in UI, up to you how you handle this
            JOptionPane.showMessageDialog(null, "Votes for story \"" + storyTitle + "\" have been revealed!");
        } else if (msg.startsWith("result:")) {
            // Format: result:room:story:avg
            String[] parts = msg.split(":");
            String storyTitle = parts[2];
            String avg = parts[3];
            JOptionPane.showMessageDialog(null, "Average vote for \"" + storyTitle + "\": " + avg);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // No-op
    }
}
