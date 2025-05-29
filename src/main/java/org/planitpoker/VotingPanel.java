package org.planitpoker;

import javax.swing.*;
import java.awt.*;

/**
 * Integrates a voting panel with the cards panel and south panel.
 *
 * @author agneskong
 */
public class VotingPanel extends JPanel {
    private VotingNanny votingNanny;

    public VotingPanel(VotingNanny votingNanny) {
        this.votingNanny = votingNanny;

        setLayout(new BorderLayout());
        add(new CardsPanel(), BorderLayout.CENTER);
        add(new EastPanel(), BorderLayout.EAST);
        add(new SouthPanel(), BorderLayout.SOUTH);

        // Demo controls for distributed voting actions:
        JPanel controlPanel = new JPanel(new GridLayout(1, 3));
        JButton sendVoteButton = new JButton("Send Vote");
        JButton revealButton = new JButton("Reveal Cards");
        JButton broadcastButton = new JButton("Broadcast Result");

        controlPanel.add(sendVoteButton);
        controlPanel.add(revealButton);
        controlPanel.add(broadcastButton);

        add(controlPanel, BorderLayout.NORTH);

        // EXAMPLE data for demonstration. Replace with actual app logic!
        String room = Blackboard.getStories().isEmpty() ? "DefaultRoom" : Blackboard.getCurrentRoom();
        String story = Blackboard.getStories().isEmpty() ? "DemoStory" : Blackboard.getStories().get(0).getTitle();
        String user = "DemoUser";
        int vote = 5;

        sendVoteButton.addActionListener(e -> votingNanny.sendEstimate(room, story, user, vote));
        revealButton.addActionListener(e -> votingNanny.revealCards(room, story));
        broadcastButton.addActionListener(e -> votingNanny.broadcastResult(room, story, 5.0));
    }
}
