package org.planitpoker;

import javax.swing.*;
import java.awt.*;

/**
 * Stories organized in tabs.
 * The first tab contains the active stories, and the second one contains the completed stories.
 *
 * @author javiergs
 */
public class SouthPanel extends JPanel {

    public SouthPanel() {
        setBackground(new Color(161, 190, 239));
        setLayout(new BorderLayout());
        JTabbedPane storyTabs = new JTabbedPane();

        JTextArea activeStories = new JTextArea();
        activeStories.setEditable(false);

        StringBuilder storyList = new StringBuilder();
        for (Story story : Blackboard.getStories()) {
            storyList.append("- ").append(story.getTitle()).append("\n");
        }
        activeStories.setText(storyList.toString());

        storyTabs.addTab("Active Stories", new JScrollPane(activeStories));
        storyTabs.addTab("Completed Stories", new JScrollPane(new JTextArea("")));
        storyTabs.addTab("All Stories", new JScrollPane(new JTextArea("")));

        add(storyTabs, BorderLayout.CENTER);
    }

}
