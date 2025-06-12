package org.planitpoker;

import javax.swing.*;
import java.awt.*;

/**
 * StoriesPanel provides the GUI for users to create, import, and manage stories
 * in the PlanItPoker application. It includes a text area for story entry,
 * buttons to save stories, import from Taiga, cancel, and start voting.
 *
 * This panel interacts with StoriesNanny to perform the underlying logic.
 *
 * Author: Agnes Kong
 * Date: June 12, 2025
 */

public class StoriesPanel extends JPanel {

    public StoriesPanel(StoriesNanny storiesNanny) {
        setBackground(new Color(245, 248, 255));
        setLayout(new BorderLayout(20, 20));

        JLabel titleLabel = new JLabel("Create New Story", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JTextArea storyTextArea = new JTextArea("Put your stories text here. Each line contains new story.");
        storyTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(storyTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 12, 8));
        buttonPanel.setOpaque(false);
        JButton saveAddNewButton = new JButton("Save & Add New");
        JButton saveCloseButton = new JButton("Save & Close");
        JButton importButton = new JButton("Import");
        JButton cancelButton = new JButton("Cancel");
        JButton startVotingButton = new JButton("Start Voting");

        buttonPanel.add(saveAddNewButton);
        buttonPanel.add(saveCloseButton);
        buttonPanel.add(importButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(startVotingButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveAddNewButton.addActionListener(e -> storiesNanny.saveAndAddNew(storyTextArea));
        saveCloseButton.addActionListener(e -> storiesNanny.saveAndClose(storyTextArea));
        importButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            storiesNanny.importStories(parentFrame, storyTextArea);
        });
        cancelButton.addActionListener(e -> storiesNanny.cancel());
        startVotingButton.addActionListener(e -> storiesNanny.goToVotingPanel());
    }
}
