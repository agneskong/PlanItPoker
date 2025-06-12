package org.planitpoker;



import javax.swing.*;
import java.awt.*;

/**
 * The class defines the main dashboard UI in the Planning Poker application.
 * It is composed of the card selection area, the south panel for auxiliary controls, and the west panel for user interactions.
 * This panel is shown after a user has joined or created a room.
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public class DashboardPanel extends JPanel {

    public DashboardPanel(DashboardNanny dashboardNanny, Main main, LoginNanny loginNanny) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 248, 255));
        add(new CardsPanel(), BorderLayout.CENTER);
        add(new SouthPanel(), BorderLayout.SOUTH);
        add(new WestPanel(dashboardNanny, main, loginNanny), BorderLayout.EAST);
    }
}
