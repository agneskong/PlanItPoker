package org.planitpoker;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;


/**
 * VotingPanel is responsible for handling the voting user interface
 * vote submission, and updating the results in real time
 *
 * @author Sathvik Chilakala
 */


public class VotingPanel extends JPanel {
    private static VotingNanny votingNannyStatic;
    private String storyTitle;
    private int storyIndex = 0;
    private EastPanel eastPanel;

    public VotingPanel(VotingNanny votingNanny) {
        votingNannyStatic = votingNanny;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 248, 255));

        JLabel titleLabel = new JLabel("Voting", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        add(titleLabel, BorderLayout.NORTH);

        LinkedList<Story> stories = Blackboard.getStories();
        if (!stories.isEmpty()) {
            storyTitle = stories.get(storyIndex).getTitle();
        } else {
            storyTitle = "No Stories";
        }

        JPanel cardPanel = new JPanel(new GridLayout(4, 3, 14, 14));
        cardPanel.setBackground(new Color(245, 248, 255));
        String[] CARD_VALUES = {"0", "½", "1", "2", "3", "5", "8", "20", "40", "100", "?", "☕"};
        for (String value : CARD_VALUES) {
            JButton card = new JButton(value);
            card.setBackground(Color.WHITE);
            card.setFocusPainted(false);
            card.setFont(new Font("Segoe UI", Font.BOLD, 24));
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 2, true),
                BorderFactory.createEmptyBorder(18, 0, 18, 0)
            ));
            card.addActionListener(e -> {
                try {
                    int val = value.equals("½") ? 1 : (value.matches("\\d+") ? Integer.parseInt(value) : 0);
                    String user = Blackboard.getNames().isEmpty() ? "DemoUser" : Blackboard.getNames().getLast();
                    votingNanny.sendEstimate(Blackboard.getCurrentRoom(), storyTitle, user, val);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            cardPanel.add(card);
        }
        add(cardPanel, BorderLayout.CENTER);

        eastPanel = new EastPanel(storyTitle, this::handleNextStory);
        add(eastPanel, BorderLayout.EAST);

        add(new SouthPanel(), BorderLayout.SOUTH);

        Timer timer = new Timer(1000, e -> eastPanel.updateStats());
        timer.start();
    }

    public static VotingNanny getVotingNannyStatic() {
        return votingNannyStatic;
    }

    public void handleNextStory(Void v) {
        LinkedList<Story> stories = Blackboard.getStories();
        if (stories.isEmpty()) return;
        Story currentStory = stories.get(storyIndex);
        if (currentStory.getVotes().keySet().containsAll(Blackboard.getNames())) {
            currentStory.markCompleted();
        }
        if (storyIndex < stories.size() - 1) {
            storyIndex++;
        } else {
            storyIndex = 0;
        }
        storyTitle = stories.get(storyIndex).getTitle();
        eastPanel.setStoryTitle(storyTitle);
        eastPanel.updateStats();
        revalidate();
        repaint();
    }
}
