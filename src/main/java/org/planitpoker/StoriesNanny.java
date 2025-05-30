package org.planitpoker;


import javax.swing.*;


public class StoriesNanny {

    private Main main;

    public StoriesNanny(Main main) {
        this.main = main;
    }

    public void saveAndAddNew(JTextArea storyTextArea) {
        String text = storyTextArea.getText();
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                Blackboard.addStory(new Story(line));
                // NEW: Publish story creation event
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

    public void importStories() {
        System.out.println("importing stories...");
    }

    public void cancel() {
        System.out.println("canceling...");
    }

    private void switchGUI() {
        main.setTitle("dashboard");
        DashboardNanny dashboardNanny = new DashboardNanny(main);
        DashboardPanel dashboardPanel = new DashboardPanel(dashboardNanny);
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