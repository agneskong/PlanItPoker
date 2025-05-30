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
        Blackboard.addStory(new Story("Build login screen"));
        Blackboard.addStory(new Story("Create dashboard page"));
        Blackboard.addStory(new Story("Implement voting backend"));

        JFrame frame = new JFrame("PlanItPoker Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.add(new CardsPanel(), BorderLayout.CENTER);
        String storyTitle = Blackboard.getStories().isEmpty() ? "No Stories" : Blackboard.getStories().get(0).getTitle();
        frame.add(new EastPanel(storyTitle, v -> {}), BorderLayout.EAST);
        frame.add(new SouthPanel(), BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}