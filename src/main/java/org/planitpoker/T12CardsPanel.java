package org.planitpoker;

import javax.swing.*;
import java.awt.*;

/**
 * The class represents the UI component that displays
 * a grid of Planning Poker cards. Each card is a button with a predefined
 * value (e.g., Fibonacci numbers, '?', '☕') used during estimation sessions.
 * The panel arranges the cards in a 4x3 grid and styles them uniformly.
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public class T12CardsPanel extends JPanel {

    private static final String[] CARD_VALUES = {
            "0", "½", "1", "2", "3", "5", "8", "20", "40", "100", "?", "☕"
    };

    public T12CardsPanel() {
        setLayout(new GridLayout(4, 3, 10, 10));
        for (String value : CARD_VALUES) {
            JButton card = new JButton(value);
            card.setBackground(new Color(172, 248, 199));
            card.setFont(new Font("SansSerif", Font.BOLD, 20));
            add(card);
        }
    }

}