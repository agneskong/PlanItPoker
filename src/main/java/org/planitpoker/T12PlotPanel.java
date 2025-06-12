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

/**
 * PlotPanel is a Swing JPanel that visualizes voting data for planning poker stories.
 *
 * It displays three charts side-by-side:
 * - A pie chart showing the vote distribution for the current story.
 * - A bar chart showing the number of votes each story has received.
 * - A bar chart showing the number of votes cast by each team member across all stories.
 *
 * The panel also includes the title of the current story at the top and a button
 * at the bottom to return to the voting interface via a callback.
 *
 * This panel uses JFreeChart to generate and render charts.
 *
 * Usage:
 * - Construct with the current Story, a list of all Stories, and a Runnable callback
 *   to invoke when returning to voting.
 *
 * Author: Sathvik Chilakala
 * Date: June 12, 2025
 */


public class T12PlotPanel extends JPanel {
    public T12PlotPanel(T12Story story, LinkedList<T12Story> allStories, Runnable returnToVotingCallback) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 248, 255));

        JLabel storyTitleLabel = new JLabel(story.getTitle(), SwingConstants.CENTER);
        storyTitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        storyTitleLabel.setForeground(new Color(255, 140, 0));
        storyTitleLabel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        add(storyTitleLabel, BorderLayout.NORTH);

        JPanel chartsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        chartsPanel.setBackground(new Color(245, 248, 255));

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
        for (T12Story s : allStories) {
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
        for (T12Story s : allStories) {
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