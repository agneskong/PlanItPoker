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

public class T12DashboardPanel extends JPanel {

    public T12DashboardPanel(T12DashboardNanny dashboardNanny, T12Main main, T12LoginNanny loginNanny) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 248, 255));
        add(new T12CardsPanel(), BorderLayout.CENTER);
        add(new T12SouthPanel(), BorderLayout.SOUTH);
        add(new T12WestPanel(dashboardNanny, main, loginNanny), BorderLayout.EAST);
    }
}
