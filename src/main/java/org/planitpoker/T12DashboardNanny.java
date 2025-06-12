package org.planitpoker;

/**
 * The class handles transitions from the dashboard to the voting interface
 * in the Planning Poker application. It coordinates GUI updates when the user initiates a voting session.
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public class T12DashboardNanny {
    private T12Main main;
    private T12LoginNanny loginNanny;

    public T12DashboardNanny(T12Main main, T12LoginNanny loginNanny) {
        this.main = main;
        this.loginNanny = loginNanny;
    }

    public void startButton() {
        switchGUI();
    }

    private void switchGUI() {
        main.setTitle("voting");
        T12VotingNanny votingNanny = new T12VotingNanny(main);
        T12VotingPanel votingPanel = new T12VotingPanel(votingNanny);

        main.setContentPane(votingPanel);
        main.setSize(800, 600);
        main.setLocationRelativeTo(null);
        main.revalidate();
        main.repaint();
    }
}
