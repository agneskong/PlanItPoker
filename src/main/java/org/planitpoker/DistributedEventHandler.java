package org.planitpoker;


import java.awt.Window;
import org.eclipse.paho.client.mqttv3.*;
import javax.swing.*;
import java.awt.*;
import org.planitpoker.Logger;
import java.util.HashMap;
import java.util.Map;


public class DistributedEventHandler implements MqttCallback {
    private MQTTSubscriber subscriber;
    private Main mainFrame;
    private interface MessageHandler { void handle(String msg); }
    private final Map<String, MessageHandler> handlerMap = new HashMap<>();

    public DistributedEventHandler(String topic, Main mainFrame) throws MqttException {
        this.mainFrame = mainFrame;
        subscriber = new MQTTSubscriber(topic, this);
        handlerMap.put("request-stories:", this::handleRequestStories);
        handlerMap.put("create-room:", this::handleCreateRoom);
        handlerMap.put("sync-stories:", this::handleSyncStories);
        handlerMap.put("request-users:", this::handleRequestUsers);
        handlerMap.put("sync-users:", this::handleSyncUsers);
        handlerMap.put("join-room:", this::handleJoinRoom);
        handlerMap.put("logout:", this::handleLogout);
        handlerMap.put("add-story:", this::handleAddStory);
        handlerMap.put("estimate:", this::handleEstimate);
        handlerMap.put("reveal:", this::handleReveal);
        handlerMap.put("result:", this::handleResult);
    }

    @Override
    public void connectionLost(Throwable cause) {
        Logger.getLogger().warn("MQTT connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String msg = new String(message.getPayload());
        Logger.getLogger().debug("MQTT RECEIVED " + msg);
        for (Map.Entry<String, MessageHandler> entry : handlerMap.entrySet()) {
            if (msg.startsWith(entry.getKey())) {
                entry.getValue().handle(msg);
                return;
            }
        }
    }

    private void handleRequestStories(String msg) {
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
    }

    private void handleCreateRoom(String msg) {
        String room = msg.substring("create-room:".length());
        Blackboard.addCurrentRoom(room);
    }

    private void handleSyncStories(String msg) {
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

    private void handleRequestUsers(String msg) {
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

    private void handleSyncUsers(String msg) {
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
                SwingUtilities.invokeLater(() -> {
                    for (Window w : Window.getWindows()) {
                        for (java.awt.Component c : w.getComponents()) {
                            refreshIfPanel(c);
                        }
                    }
                });
            }
        }
    }

    private void handleJoinRoom(String msg) {
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
    }

    private void handleLogout(String msg) {
        String[] parts = msg.split(":");
        if (parts.length >= 3) {
            String user = parts[2];
            Blackboard.getNames().remove(user);
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

    private void handleAddStory(String msg) {
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
        refreshAllStoryPanels();
    }

    private void handleEstimate(String msg) {
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
    }

    private void handleReveal(String msg) {
        String[] parts = msg.split(":");
        if (parts.length >= 4) {
            String room = parts[1];
            String storyTitle = parts[2];
            String revealUser = parts[3];
            String currentUser = Blackboard.getNames().isEmpty() ? "" : Blackboard.getNames().getLast();
            if (room.equals(Blackboard.getCurrentRoom()) && revealUser.equals(currentUser)) {
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
            } else if (room.equals(Blackboard.getCurrentRoom())) {
                for (Story s : Blackboard.getStories()) {
                    if (s.getTitle().equals(storyTitle)) {
                        s.markCompleted();
                        refreshAllStoryPanels();
                    }
                }
            }
        }
    }

    private void handleResult(String msg) {
        String[] parts = msg.split(":");
        if (parts.length >= 5) {
            String room = parts[1];
            String storyTitle = parts[2];
            String avg = parts[3];
            String resultUser = parts[4];
            String currentUser = Blackboard.getNames().isEmpty() ? "" : Blackboard.getNames().getLast();
            if (room.equals(Blackboard.getCurrentRoom()) && resultUser.equals(currentUser)) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(mainFrame,
                        "Average vote for story '" + storyTitle + "': " + avg);
                    refreshAllStoryPanels();
                });
            } else if (room.equals(Blackboard.getCurrentRoom())) {
                refreshAllStoryPanels();
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
