package org.planitpoker;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PlotPanel extends JPanel {
    public PlotPanel(Story story, LinkedList<Story> allStories, Runnable returnToVotingCallback) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 248, 255));

        // Top: Story title
        JLabel storyTitleLabel = new JLabel(story.getTitle(), SwingConstants.CENTER);
        storyTitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        storyTitleLabel.setForeground(new Color(255, 140, 0));
        storyTitleLabel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        add(storyTitleLabel, BorderLayout.NORTH);

        // Center: Charts
        JPanel chartsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        chartsPanel.setBackground(new Color(245, 248, 255));

        // Pie chart: vote distribution for this story
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        Map<Integer, Integer> voteCounts = new HashMap<>();
        for (Integer vote : story.getVotes().values()) {
            voteCounts.put(vote, voteCounts.getOrDefault(vote, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : voteCounts.entrySet()) {
            pieDataset.setValue(entry.getKey() + "", entry.getValue());
        }
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Vote Distribution",
                pieDataset, true, true, false);
        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint(new Color(245, 248, 255));
        piePlot.setSectionPaint(0, new Color(255, 180, 60)); // orange
        piePlot.setOutlineVisible(false);
        chartsPanel.add(new ChartPanel(pieChart));

        // Bar chart: votes per story
        DefaultCategoryDataset storyDataset = new DefaultCategoryDataset();
        for (Story s : allStories) {
            storyDataset.addValue(s.getVotes().size(), "Votes", s.getTitle());
        }
        JFreeChart storyBarChart = ChartFactory.createBarChart(
                "Votes per Story", "Story", "Votes", storyDataset);
        CategoryPlot storyPlot = (CategoryPlot) storyBarChart.getPlot();
        storyPlot.setBackgroundPaint(new Color(245, 248, 255));
        storyPlot.getRenderer().setSeriesPaint(0, new Color(255, 180, 60));
        chartsPanel.add(new ChartPanel(storyBarChart));

        // Bar chart: votes per team member (across all stories)
        DefaultCategoryDataset userDataset = new DefaultCategoryDataset();
        Map<String, Integer> userVotes = new HashMap<>();
        for (Story s : allStories) {
            for (String user : s.getVotes().keySet()) {
                userVotes.put(user, userVotes.getOrDefault(user, 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : userVotes.entrySet()) {
            userDataset.addValue(entry.getValue(), "Votes", entry.getKey());
        }
        JFreeChart userBarChart = ChartFactory.createBarChart(
                "Votes per Team Member", "User", "Votes", userDataset);
        CategoryPlot userPlot = (CategoryPlot) userBarChart.getPlot();
        userPlot.setBackgroundPaint(new Color(245, 248, 255));
        userPlot.getRenderer().setSeriesPaint(0, new Color(255, 180, 60));
        chartsPanel.add(new ChartPanel(userBarChart));

        add(chartsPanel, BorderLayout.CENTER);

        // Bottom: Return to Voting button
        JButton returnButton = new JButton("Return to Voting");
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        returnButton.setBackground(new Color(255, 180, 60));
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(e -> returnToVotingCallback.run());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(returnButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
} 