package org.planitpoker;

import org.eclipse.paho.client.mqttv3.*;
import javax.swing.*;
import java.awt.Container;

public class DistributedEventHandler implements MqttCallback {
    private MQTTSubscriber subscriber;
    private Main mainFrame;

    public DistributedEventHandler(String topic, Main mainFrame) throws MqttException {
        this.mainFrame = mainFrame;
        subscriber = new MQTTSubscriber(topic, this);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("MQTT connection lost!");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String msg = new String(message.getPayload());
        System.out.println("[MQTT RECEIVED] " + msg);

        if (msg.startsWith("create-room:")) {
            String room = msg.substring("create-room:".length());
            Blackboard.addCurrentRoom(room);

        } else if (msg.startsWith("join-room:")) {
            String[] parts = msg.split(":");
            if (parts.length >= 3) {
                String user = parts[2];
                Blackboard.addName(user);
            }

        } else if (msg.startsWith("add-story:")) {
            String[] parts = msg.split(":");
            if (parts.length >= 3) {
                String room = parts[1];
                String storyTitle = parts[2];
                if (room.equals(Blackboard.getCurrentRoom())) {
                    boolean exists = false;
                    for (Story s : Blackboard.getStories()) {
                        if (s.getTitle().equals(storyTitle)) {
                            exists = true; break;
                        }
                    }
                    if (!exists) {
                        Blackboard.addStory(new Story(storyTitle));
                    }
                }
            }

        } else if (msg.startsWith("estimate:")) {
            String[] parts = msg.split(":");
            if (parts.length >= 5) {
                String room = parts[1];
                String storyTitle = parts[2];
                String user = parts[3];
                int vote = Integer.parseInt(parts[4]);
                if (room.equals(Blackboard.getCurrentRoom())) {
                    Story found = null;
                    for (Story s : Blackboard.getStories()) {
                        if (s.getTitle().equals(storyTitle)) {
                            found = s; break;
                        }
                    }
                    if (found == null) {
                        found = new Story(storyTitle);
                        Blackboard.addStory(found);
                    }
                    found.submitVotes(user, vote);
                    refreshVotingPanel();
                }
            }

        } else if (msg.startsWith("reveal:")) {
            String[] parts = msg.split(":");
            if (parts.length >= 3) {
                String room = parts[1];
                String storyTitle = parts[2];
                if (room.equals(Blackboard.getCurrentRoom())) {
                    for (Story s : Blackboard.getStories()) {
                        if (s.getTitle().equals(storyTitle)) {
                            s.markCompleted();
                            StringBuilder votesMsg = new StringBuilder();
                            for (String user : s.getVotes().keySet()) {
                                votesMsg.append(user).append(": ").append(s.getVotes().get(user)).append("\n");
                            }
                            double avg = s.calculateAverage();
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(mainFrame,
                                    "Votes revealed for story '" + storyTitle + "':\n" + votesMsg + String.format("Average: %.2f", avg));
                                refreshVotingPanel();
                            });
                        }
                    }
                }
            }

        } else if (msg.startsWith("result:")) {
            String[] parts = msg.split(":");
            if (parts.length >= 4) {
                String room = parts[1];
                String storyTitle = parts[2];
                String avg = parts[3];
                if (room.equals(Blackboard.getCurrentRoom())) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(mainFrame,
                            "Average vote for story '" + storyTitle + "': " + avg);
                        refreshVotingPanel();
                    });
                }
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) { }

    private void refreshVotingPanel() {
        SwingUtilities.invokeLater(() -> {
            VotingNanny votingNanny = new VotingNanny(mainFrame);
            VotingPanel votingPanel = new VotingPanel(votingNanny);
            mainFrame.setContentPane(votingPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }

    private void refreshStoriesPanel() {
        SwingUtilities.invokeLater(() -> {
            if (mainFrame.getContentPane() instanceof StoriesPanel) {
                StoriesNanny storiesNanny = new StoriesNanny(mainFrame);
                StoriesPanel storiesPanel = new StoriesPanel(storiesNanny);
                mainFrame.setContentPane(storiesPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            } else if (mainFrame.getContentPane() instanceof DashboardPanel) {
                DashboardNanny dashboardNanny = new DashboardNanny(mainFrame);
                DashboardPanel dashboardPanel = new DashboardPanel(dashboardNanny);
                mainFrame.setContentPane(dashboardPanel);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });
    }
}
