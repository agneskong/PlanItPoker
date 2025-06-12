package org.planitpoker;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.HashSet; // <-- ADD THIS!

/**
 * SouthPanel is a Swing JPanel that displays stories categorized into Active, Completed, and All stories
 * using a tabbed pane. It refreshes its content every second to stay up-to-date with the current
 * state of stories stored in the Blackboard.
 *
 * It maintains a static set of all SouthPanel instances for centralized refresh control.
 *
 * Purpose: To provide a continuously updated overview of story statuses in the planning poker app.
 *
 * Author: Agnes Kong
 * Date: June 12, 2025
 */


public class T12SouthPanel extends JPanel {
    private JTabbedPane storyTabs;
    private JTextArea activeStories;
    private JTextArea completedStories;
    private JTextArea allStories;
    public static final Set<T12SouthPanel> INSTANCES = new HashSet<>();

    public T12SouthPanel() {
        INSTANCES.add(this);
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

        for (T12Story story : T12Blackboard.getStories()) {
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

    public static void refreshAllPanels() {
        for (T12SouthPanel panel : INSTANCES) {
            panel.refreshStories();
        }
    }

    @Override
    public void removeNotify() {
        INSTANCES.remove(this);
        super.removeNotify();
    }
}
