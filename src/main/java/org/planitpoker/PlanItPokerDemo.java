package org.planitpoker;

import javax.swing.*;
import java.awt.*;

/**
 * PlanItPokerVotingDemo
 *
 * This demo allows participants to see the voting panel. It DOES
 * NOT demonstrate the logic behind voting. That is demonstrated in PlanItPokerVotingDemo.
 *
 * Satisfies:
 * - Task #47: Show the average result
 * - Task #63: Display list of stories in a table or card list view.
 * - Task #44: Display votes in a list
 * - Task #42: Show the Reveal Votes Button
 *
 * @author agneskong
 */

public class PlanItPokerDemo {

    public static void main(String[] args) {
        T12Blackboard.addStory(new T12Story("Build login screen"));
        T12Blackboard.addStory(new T12Story("Create dashboard page"));
        T12Blackboard.addStory(new T12Story("Implement voting backend"));

        JFrame frame = new JFrame("PlanItPoker Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.add(new T12CardsPanel(), BorderLayout.CENTER);
        String storyTitle = T12Blackboard.getStories().isEmpty() ? "No Stories" : T12Blackboard.getStories().get(0).getTitle();
        frame.add(new T12EastPanel(storyTitle, v -> {}), BorderLayout.EAST);
        frame.add(new T12SouthPanel(), BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}