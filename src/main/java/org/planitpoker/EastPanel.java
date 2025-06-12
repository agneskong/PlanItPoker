package org.planitpoker;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * EastPanel is a Swing UI component in the PlanitPoker application that manages the display
 * of voting-related information for a selected user story. This panel includes buttons for revealing
 * votes, moving to the next story, and logging out. It shows the current votes, average score,
 * users in the room, and which users have already voted. It also communicates with other components
 * such as the VotingPanel and Main application frame to perform story navigation and session management.
 *
 * This panel is designed to update dynamically based on events received via MQTT and user interactions.
 *
 * Author: Sathvik Chilakala
 * Date: June 12, 2025
 */

public class EastPanel extends JPanel {
    private JLabel votesLabel;
    private JLabel averageLabel;
    private String storyTitle;
    private Consumer<Void> nextStoryHandler;
    private JTextArea playersArea;
    private JTextArea votedArea;

    public EastPanel(String storyTitle, Consumer<Void> nextStoryHandler) {
        this.storyTitle = storyTitle;
        this.nextStoryHandler = nextStoryHandler;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 16, 0, 16));
        setBackground(new Color(237, 244, 255));

        JButton revealButton = new JButton("Reveal Votes");
        revealButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        revealButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton nextButton = new JButton("Next Story");
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        votesLabel = new JLabel("Votes: ");
        votesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        votesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        averageLabel = new JLabel("Average: ");
        averageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        averageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Players label & list
        JLabel playersLabel = new JLabel("Players in Room:");
        playersLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        playersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playersArea = new JTextArea();
        playersArea.setEditable(false);
        playersArea.setBackground(new Color(237, 244, 255));
        playersArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        playersArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        playersArea.setMaximumSize(new Dimension(180, 80));

        JLabel votedLabel = new JLabel("Voted:");
        votedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        votedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        votedArea = new JTextArea();
        votedArea.setEditable(false);
        votedArea.setBackground(new Color(237, 244, 255));
        votedArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        votedArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        votedArea.setMaximumSize(new Dimension(180, 80));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(16));
        add(revealButton);
        add(Box.createVerticalStrut(12));
        add(nextButton);
        add(Box.createVerticalStrut(20));
        add(votesLabel);
        add(Box.createVerticalStrut(8));
        add(averageLabel);
        add(Box.createVerticalStrut(20));
        add(playersLabel);
        add(playersArea);
        add(Box.createVerticalStrut(8));
        add(votedLabel);
        add(votedArea);
        add(Box.createVerticalStrut(6));
        add(logoutButton);

        updateStats();
        updatePlayers();

        revealButton.addActionListener(e -> {
            Story currentStory = findStory();
            if (currentStory != null) {
                try {
                    VotingNanny votingNanny = VotingPanel.getVotingNannyStatic();
                    if (votingNanny != null) {
                        votingNanny.revealCards(Blackboard.getCurrentRoom(), currentStory.getTitle());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        nextButton.addActionListener(e -> {
            if (nextStoryHandler != null) nextStoryHandler.accept(null);
        });

        logoutButton.addActionListener(e -> {
            // Use main frame's login nanny to logout and switch GUI
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof Main) {
                ((Main) topFrame).getLoginNanny().logout();
            }
        });
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public void updateStats() {
        Story story = findStory();
        if (story != null) {
            StringBuilder sb = new StringBuilder();
            for (String user : story.getVotes().keySet()) {
                sb.append(story.getVotes().get(user)).append(", ");
            }
            String voteList = sb.length() > 2 ? sb.substring(0, sb.length() - 2) : "";
            votesLabel.setText("Votes: " + voteList);
            averageLabel.setText(String.format("Average: %.2f", story.calculateAverage()));
        } else {
            votesLabel.setText("Votes: N/A");
            averageLabel.setText("Average: N/A");
        }
    }

    // --- New: Update players ---
    public void updatePlayers() {
        // Show all users in the room
        StringBuilder sb = new StringBuilder();
        for (String name : Blackboard.getNames()) {
            sb.append(name).append("\n");
        }
        playersArea.setText(sb.toString());

        // Show users who have voted for the current story
        Story story = findStory();
        StringBuilder votedSb = new StringBuilder();
        if (story != null) {
            for (String user : story.getVotes().keySet()) {
                votedSb.append(user).append("\n");
            }
        }
        votedArea.setText(votedSb.toString());
    }

    private Story findStory() {
        for (Story s : Blackboard.getStories()) {
            if (s.getTitle().equals(storyTitle)) return s;
        }
        return null;
    }

    // --- Called by DistributedEventHandler ---
    public void refreshStories() {
        updatePlayers();
        updateStats();
    }
}
