package org.planitpoker;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class EastPanel extends JPanel {
    private JLabel votesLabel;
    private JLabel averageLabel;
    private String storyTitle;
    private Consumer<Void> nextStoryHandler;

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

        add(Box.createVerticalStrut(16));
        add(revealButton);
        add(Box.createVerticalStrut(12));
        add(nextButton);
        add(Box.createVerticalStrut(22));
        add(votesLabel);
        add(Box.createVerticalStrut(8));
        add(averageLabel);

        updateStats();

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

    private Story findStory() {
        for (Story s : Blackboard.getStories()) {
            if (s.getTitle().equals(storyTitle)) return s;
        }
        return null;
    }
}
