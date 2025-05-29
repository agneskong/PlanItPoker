package org.planitpoker;

import javax.swing.*;
import java.awt.*;

/**
 * Integrates a dashboard with the cards, timer, and stories.
 *
 * @author javiergs
 */
public class DashboardPanel extends JPanel {

    public DashboardPanel(DashboardNanny dashboardNanny) {
        setLayout(new BorderLayout());
        add(new CardsPanel(), BorderLayout.CENTER);
        add(new SouthPanel(), BorderLayout.SOUTH);
        add(new WestPanel(dashboardNanny), BorderLayout.EAST);


    }

}
