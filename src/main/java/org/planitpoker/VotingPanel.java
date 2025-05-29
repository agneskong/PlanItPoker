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
        setLayout(new BorderLayout());
        add(new CardsPanel(), BorderLayout.CENTER);
        add(new EastPanel(), BorderLayout.EAST);
        add(new SouthPanel(), BorderLayout.SOUTH);


    }
}

