package org.planitpoker;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(DashboardNanny dashboardNanny) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 248, 255));
        add(new CardsPanel(), BorderLayout.CENTER);
        add(new SouthPanel(), BorderLayout.SOUTH);
        add(new WestPanel(dashboardNanny), BorderLayout.EAST);
    }
}
