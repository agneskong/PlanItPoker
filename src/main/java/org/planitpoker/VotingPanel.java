package org.planitpoker;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

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

    // Timer related
    private JLabel timerLabel;
    private Timer timer;
    private long startTime;
    private Map<String, Map<String, Long>> voteTimes = new HashMap<>(); // user -> storyTitle -> time

    private JButton showDataButton;
    private JPanel cardPanel;
    private JLabel titleLabel;

    public VotingPanel(VotingNanny votingNanny) {
        votingNannyStatic = votingNanny;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 248, 255));

        // Top panel with title, timer, and Show Data button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        titleLabel = new JLabel("Voting", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        timerLabel = new JLabel("Elapsed: 00:00");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        timerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(timerLabel, BorderLayout.WEST);

        showDataButton = new JButton("Show Data");
        showDataButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        showDataButton.setBackground(new Color(255, 180, 60));
        showDataButton.setFocusPainted(false);
        showDataButton.addActionListener(e -> showResultsChart());
        JPanel showDataPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        showDataPanel.setOpaque(false);
        showDataPanel.add(showDataButton);
        topPanel.add(showDataPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        LinkedList<Story> stories = Blackboard.getStories();
        if (!stories.isEmpty()) {
            storyTitle = stories.get(storyIndex).getTitle();
        } else {
            storyTitle = "No Stories";
        }

        cardPanel = new JPanel(new GridLayout(4, 3, 14, 14));
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
                    // Stop timer for this user/story and save
                    stopUserTimer(user, storyTitle);
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

        Timer eastPanelTimer = new Timer(1000, e -> eastPanel.updateStats());
        eastPanelTimer.start();

        startUserTimer();
    }

    private void startUserTimer() {
        startTime = System.currentTimeMillis();
        if (timer != null) timer.stop();
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
    }

    private void updateTimer() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        long mins = elapsed / 60;
        long secs = elapsed % 60;
        timerLabel.setText(String.format("Elapsed: %02d:%02d", mins, secs));
    }

    private void stopUserTimer(String user, String story) {
        if (timer != null) timer.stop();
        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        // Save the vote time for this user and story if needed
        if (!voteTimes.containsKey(user)) voteTimes.put(user, new HashMap<>());
        voteTimes.get(user).put(story, totalTime);
        timerLabel.setText("Voted in: " + totalTime + "s");
    }

    public static VotingNanny getVotingNannyStatic() {
        return votingNannyStatic;
    }

    public void handleNextStory(Void v) {
        LinkedList<Story> stories = Blackboard.getStories();
        if (stories.isEmpty()) return;
        Story currentStory = stories.get(storyIndex);
        // Only mark as completed and move to next on Next Story
        currentStory.markCompleted();
        if (storyIndex < stories.size() - 1) {
            storyIndex++;
        } else {
            storyIndex = 0;
        }
        storyTitle = stories.get(storyIndex).getTitle();
        eastPanel.setStoryTitle(storyTitle);
        eastPanel.updateStats();
        startUserTimer(); // Restart timer for new story
        revalidate();
        repaint();
    }

    public void refreshStories() {
        if (eastPanel != null) eastPanel.refreshStories();
    }

    private void showResultsChart() {
        removeAll();
        Story currentStory = Blackboard.getStories().get(storyIndex);
        add(new PlotPanel(currentStory, Blackboard.getStories(), this::returnToVoting), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void returnToVoting() {
        removeAll();
        // Rebuild the top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(timerLabel, BorderLayout.WEST);
        JPanel showDataPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        showDataPanel.setOpaque(false);
        showDataPanel.add(showDataButton);
        topPanel.add(showDataPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(new SouthPanel(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
