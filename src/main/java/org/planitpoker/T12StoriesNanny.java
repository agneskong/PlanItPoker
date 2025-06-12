package org.planitpoker;

import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * StoriesNanny handles operations related to managing user stories within the PlanItPoker app.
 * It supports saving new stories, importing stories from the Taiga project management system,
 * navigating between different GUI panels such as the dashboard and voting panel,
 * and publishing story-related events via MQTT.
 *
 * Responsibilities include:
 * - Saving and adding new stories to the Blackboard and publishing them.
 * - Importing stories from Taiga with authentication.
 * - Switching GUI views between dashboard and voting.
 *
 * Author: Agnes Kong
 * Date: June 12, 2025
 */

public class T12StoriesNanny {

    private T12Main main;
    private T12LoginNanny loginNanny;

    public T12StoriesNanny(T12Main main, T12LoginNanny loginNanny) {
        this.main = main;
        this.loginNanny = loginNanny;
    }

    public void saveAndAddNew(JTextArea storyTextArea) {
        String text = storyTextArea.getText();
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                T12Blackboard.addStory(new T12Story(line));
                try {
                    T12MQTTPublisher publisher = new T12MQTTPublisher();
                    String room = T12Blackboard.getCurrentRoom();
                    String msg = String.format("add-story:%s:%s", room, line);
                    publisher.publish("planitpoker/events", msg);
                    publisher.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        storyTextArea.setText("");
        JOptionPane.showMessageDialog(null, "Stories saved successfully!");
    }

    public void saveAndClose(JTextArea storyTextArea) {
        String text = storyTextArea.getText();
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                T12Blackboard.addStory(new T12Story(line));
            }
        }
        switchGUI();
    }

    public void importStories(JFrame parentFrame, JTextArea outputTextArea) {
        T12TaigaLoginDialog loginDialog = new T12TaigaLoginDialog(parentFrame);
        loginDialog.setLocationRelativeTo(parentFrame);
        loginDialog.setVisible(true);

        if (loginDialog.wasSubmitted()) {
            String username = loginDialog.getUsername();
            String password = loginDialog.getPassword();

            try {
                String token = T12TaigaStoryFetcher.loginAndGetToken(username, password);
                T12Blackboard.setAuthToken(token);
                int projectId = T12TaigaStoryFetcher.getProjectId(token, "agneskong-test-1");

                JSONArray stories = T12TaigaStoryFetcher.fetchUserStories(token, projectId);

                outputTextArea.setText("");  // ✅ Clear area and write new stories
                for (int i = 0; i < stories.length(); i++) {
                    JSONObject story = stories.getJSONObject(i);
                    outputTextArea.append("#" + story.getInt("id") + " – " + story.optString("subject", "(no title)") + "\n");
                }
            } catch (Exception ex) {
                outputTextArea.setText("❌ Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        T12Logger.getLogger().info("importing stories...");
    }


    public void cancel() {
        T12Logger.getLogger().info("canceling...");
    }

    private void switchGUI() {
        main.setTitle("dashboard");
        T12DashboardNanny dashboardNanny = new T12DashboardNanny(main, loginNanny);
        T12DashboardPanel dashboardPanel = new T12DashboardPanel(dashboardNanny, main, loginNanny);
        main.setContentPane(dashboardPanel);
        main.setSize(800, 600);
        main.setLocationRelativeTo(null);
        main.revalidate();
        main.repaint();
    }

    public void goToVotingPanel() {
        main.setTitle("Voting");
        T12VotingNanny votingNanny = new T12VotingNanny(main);
        T12VotingPanel votingPanel = new T12VotingPanel(votingNanny);
        main.setContentPane(votingPanel);
        main.setSize(900, 700);
        main.setLocationRelativeTo(null);
        main.revalidate();
        main.repaint();
    }
}
