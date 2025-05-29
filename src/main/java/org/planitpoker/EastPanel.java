package org.planitpoker;

import javax.swing.*;
import java.awt.*;

public class EastPanel extends JPanel {
    public EastPanel() {
        setLayout(new GridLayout(7,2));
        add(new JButton("Reveal Votes"));
        add(new JButton("Next Story"));
        add(new JLabel("Votes: 1, 2, 3, 4, 5"));
        add(new JLabel("Average: 3.00"));
    }
}
