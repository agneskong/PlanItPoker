package org.planitpoker;

import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.planitpoker.Logger;

public class StoriesNanny {

    private Main main;
    private LoginNanny loginNanny;

    public StoriesNanny(Main main, LoginNanny loginNanny) {
        this.main = main;
        this.loginNanny = loginNanny;
    }

    public void saveAndAddNew(JTextArea storyTextArea) {
        String text = storyTextArea.getText();
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                Blackboard.addStory(new Story(line));
                try {
                    MQTTPublisher publisher = new MQTTPublisher();
                    String room = Blackboard.getCurrentRoom();
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
                Blackboard.addStory(new Story(line));
            }
        }
        switchGUI();
    }

    public void importStories(JFrame parentFrame, JTextArea outputTextArea) {
        TaigaLoginDialog loginDialog = new TaigaLoginDialog(parentFrame);
        loginDialog.setLocationRelativeTo(parentFrame);
        loginDialog.setVisible(true);

        if (loginDialog.wasSubmitted()) {
            String username = loginDialog.getUsername();
            String password = loginDialog.getPassword();

            try {
                String token = TaigaStoryFetcher.loginAndGetToken(username, password);
                Blackboard.setAuthToken(token);
                int projectId = TaigaStoryFetcher.getProjectId(token, "agneskong-test-1");

                JSONArray stories = TaigaStoryFetcher.fetchUserStories(token, projectId);

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

        Logger.getLogger().info("importing stories...");
    }


    public void cancel() {
        Logger.getLogger().info("canceling...");
    }

    private void switchGUI() {
        main.setTitle("dashboard");
        DashboardNanny dashboardNanny = new DashboardNanny(main, loginNanny);
        DashboardPanel dashboardPanel = new DashboardPanel(dashboardNanny, main, loginNanny);
        main.setContentPane(dashboardPanel);
        main.setSize(800, 600);
        main.setLocationRelativeTo(null);
        main.revalidate();
        main.repaint();
    }

    public void goToVotingPanel() {
        main.setTitle("Voting");
        VotingNanny votingNanny = new VotingNanny(main);
        VotingPanel votingPanel = new VotingPanel(votingNanny);
        main.setContentPane(votingPanel);
        main.setSize(900, 700);
        main.setLocationRelativeTo(null);
        main.revalidate();
        main.repaint();
    }
}
