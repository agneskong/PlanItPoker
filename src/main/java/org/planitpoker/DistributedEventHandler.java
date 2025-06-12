package org.planitpoker;


import java.awt.Window;
import org.eclipse.paho.client.mqttv3.*;
import javax.swing.*;
import java.awt.*;
import org.planitpoker.Logger;


public class DistributedEventHandler implements MqttCallback {
    private MQTTSubscriber subscriber;
    private Main mainFrame;

    public DistributedEventHandler(String topic, Main mainFrame) throws MqttException {
        this.mainFrame = mainFrame;
        subscriber = new MQTTSubscriber(topic, this);
    }

    @Override
    public void connectionLost(Throwable cause) {
        Logger.getLogger().warn("MQTT connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String msg = new String(message.getPayload());
        Logger.getLogger().debug("MQTT RECEIVED " + msg);

        // Handle story synchronization request (for new users)
        if (msg.startsWith("request-stories:")) {
            String[] parts = msg.split(":");
            if (parts.length >= 3) {
                String room = parts[1];
                String user = parts[2];
                if (room.equals(Blackboard.getCurrentRoom()) && !Blackboard.getStories().isEmpty()) {
                    try {
                        MQTTPublisher publisher = new MQTTPublisher();
                        StringBuilder sb = new StringBuilder();
                        for (Story s : Blackboard.getStories()) {
                            sb.append(s.getTitle()).append("|");
                        }
                        if (sb.length() > 0) sb.setLength(sb.length() - 1);
                        String syncMsg = String.format("sync-stories:%s:%s:%s", room, user, sb.toString());
                        publisher.publish("planitpoker/events", syncMsg);
                        publisher.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (msg.startsWith("create-room:")) {
            String room = msg.substring("create-room:".length());
            Blackboard.addCurrentRoom(room);
        }

        // Handle incoming sync-stories for new user
        else if (msg.startsWith("sync-stories:")) {
            String[] parts = msg.split(":", 4);
            if (parts.length >= 4) {
                String room = parts[1];
                String user = parts[2];
                String storyList = parts[3];
                String currentUser = Blackboard.getNames().isEmpty() ? "" : Blackboard.getNames().getLast();
                if (room.equals(Blackboard.getCurrentRoom()) && user.equals(currentUser)) {
                    for (String title : storyList.split("\\|")) {
                        boolean exists = false;
                        for (Story s : Blackboard.getStories()) {
                            if (s.getTitle().equals(title)) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists && !title.trim().isEmpty()) {
                            Blackboard.addStory(new Story(title));
                        }
                    }
                    refreshAllStoryPanels();
                }
            }
        }
        // --- USERS HANDLERS, unchanged ---
        else if (msg.startsWith("request-users:")) {
            String[] parts = msg.split(":");
            if (parts.length >= 3) {
                String room = parts[1];
                String user = parts[2];
                if (room.equals(Blackboard.getCurrentRoom()) && !Blackboard.getNames().isEmpty()) {
                    try {
                        MQTTPublisher publisher = new MQTTPublisher();
                        StringBuilder sb = new StringBuilder();
                        for (String n : Blackboard.getNames()) {
                            sb.append(n).append("|");
                        }
                        if (sb.length() > 0) sb.setLength(sb.length() - 1);
                        String syncMsg = String.format("sync-users:%s:%s:%s", room, user, sb.toString());
                        publisher.publish("planitpoker/events", syncMsg);
                        publisher.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if (msg.startsWith("sync-users:")) {
            String[] parts = msg.split(":", 4);
            if (parts.length >= 4) {
                String room = parts[1];
                String user = parts[2];
                String userList = parts[3];
                if (room.equals(Blackboard.getCurrentRoom())) {
                    Blackboard.getNames().clear();
                    for (String n : userList.split("\\|")) {
                        if (!n.trim().isEmpty()) {
                            Blackboard.addName(n);
                        }
                    }
                    // Make sure to refresh all panels showing users
                    SwingUtilities.invokeLater(() -> {
                        for (Window w : Window.getWindows()) {
                            for (java.awt.Component c : w.getComponents()) {
                                refreshIfPanel(c); // Now static!
                            }
                        }
                    });
                }
            }
        }
        // --- EXISTING ROOM HANDLERS ---
        else if (msg.startsWith("create-room:")) {
            String room = msg.substring("create-room:".length());
            Blackboard.addCurrentRoom(room);

        } else if (msg.startsWith("join-room:")) {
            String[] parts = msg.split(":");
            if (parts.length >= 3) {
                String room = parts[1];
                String user = parts[2];

                if (room.equals(Blackboard.getCurrentRoom()) && !user.equals(room)) {
                    if (!Blackboard.getNames().contains(user)) {
                        Blackboard.addName(user);
                    }

                    try {
                        MQTTPublisher publisher = new MQTTPublisher();
                        StringBuilder sb = new StringBuilder();
                        for (String n : Blackboard.getNames()) {
                            sb.append(n).append("|");
                        }
                        if (sb.length() > 0) sb.setLength(sb.length() - 1);
                        String syncMsg = String.format("sync-users:%s:%s:%s", room, user, sb.toString());
                        publisher.publish("planitpoker/events", syncMsg);
                        publisher.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

    } else if (msg.startsWith("logout:")) {
            String[] parts = msg.split(":");
            if (parts.length >= 3) {
                String user = parts[2];
                Blackboard.getNames().remove(user);
                // Broadcast updated user list to all clients in the room
                String room = Blackboard.getCurrentRoom();
                if (room != null) {
                    try {
                        MQTTPublisher publisher = new MQTTPublisher();
                        StringBuilder sb = new StringBuilder();
                        for (String n : Blackboard.getNames()) {
                            sb.append(n).append("|");
                        }
                        if (sb.length() > 0) sb.setLength(sb.length() - 1);
                        String syncMsg = String.format("sync-users:%s:%s:%s", room, user, sb.toString());
                        publisher.publish("planitpoker/events", syncMsg);
                        publisher.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // --- STORY ADDED HANDLER ---
        else if (msg.startsWith("add-story:")) {
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
                    if (!exists && !storyTitle.trim().isEmpty()) {
                        Blackboard.addStory(new Story(storyTitle));
                    }
                }
            }
            // --- Ensure every screen (Dashboard, Voting, Stories, SouthPanel etc.) gets updated ---
            refreshAllStoryPanels();
        } 
        // --- VOTING HANDLERS (unchanged) ---
        else if (msg.startsWith("estimate:")) {
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
                    refreshAllStoryPanels();
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
                                refreshAllStoryPanels();
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
                        refreshAllStoryPanels();
                    });
                }
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) { }

    /** 
     * This is the key method: 
     * Any time a story or user is added or changed, this will trigger 
     * every visible story/user-related panel to update!
     */
    private static void refreshAllStoryPanels() {
        SwingUtilities.invokeLater(() -> {
            for (Window w : Window.getWindows()) {
                for (java.awt.Component c : w.getComponents()) {
                    refreshIfPanel(c);
                }
            }
        });
    }

    // Helper for recursive refresh
    private static void refreshIfPanel(java.awt.Component comp) {
        if (comp instanceof JFrame) {
            JFrame frame = (JFrame) comp;
            if (frame.getContentPane() != null) {
                refreshIfPanel(frame.getContentPane());
            }
        } else if (comp instanceof JTabbedPane) {
            JTabbedPane tabs = (JTabbedPane) comp;
            for (int i = 0; i < tabs.getTabCount(); ++i) {
                refreshIfPanel(tabs.getComponentAt(i));
            }
        } else if (comp instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) comp;
            refreshIfPanel(scroll.getViewport().getView());
        } else if (comp instanceof JPanel) {
            JPanel panel = (JPanel) comp;
            // Panels that display stories should implement a refreshStories() method.
            try {
                java.lang.reflect.Method m = panel.getClass().getMethod("refreshStories");
                m.invoke(panel);
            } catch (Exception ignored) { }
            // Recursively look for story panels inside this panel.
            for (java.awt.Component child : panel.getComponents()) {
                refreshIfPanel(child);
            }
        }
    }
}
