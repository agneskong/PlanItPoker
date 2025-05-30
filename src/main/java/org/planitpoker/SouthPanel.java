package org.planitpoker;

import javax.swing.*;
import java.awt.*;

public class SouthPanel extends JPanel {
    private JTabbedPane storyTabs;
    private JTextArea activeStories;
    private JTextArea completedStories;
    private JTextArea allStories;

    public SouthPanel() {
        setBackground(new Color(245, 248, 255));
        setLayout(new BorderLayout());
        storyTabs = new JTabbedPane();

        activeStories = new JTextArea();
        activeStories.setEditable(false);
        completedStories = new JTextArea();
        completedStories.setEditable(false);
        allStories = new JTextArea();
        allStories.setEditable(false);

        refreshStories();

        storyTabs.addTab("Active Stories", new JScrollPane(activeStories));
        storyTabs.addTab("Completed Stories", new JScrollPane(completedStories));
        storyTabs.addTab("All Stories", new JScrollPane(allStories));
        add(storyTabs, BorderLayout.CENTER);

        new Timer(1000, e -> refreshStories()).start();
    }

    public void refreshStories() {
        StringBuilder active = new StringBuilder();
        StringBuilder completed = new StringBuilder();
        StringBuilder all = new StringBuilder();

        for (Story story : Blackboard.getStories()) {
            all.append("- ").append(story.getTitle()).append("\n");
            if (story.isActive()) {
                active.append("- ").append(story.getTitle()).append("\n");
            } else {
                completed.append("- ").append(story.getTitle()).append("\n");
            }
        }
        activeStories.setText(active.toString());
        completedStories.setText(completed.toString());
        allStories.setText(all.toString());
    }
}
