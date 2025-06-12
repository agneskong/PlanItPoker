package org.planitpoker;

/**
 * The class handles transitions from the dashboard to the voting interface
 * in the Planning Poker application. It coordinates GUI updates when the user initiates a voting session.
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public class DashboardNanny {
    private Main main;
    private LoginNanny loginNanny;

    public DashboardNanny(Main main, LoginNanny loginNanny) {
        this.main = main;
        this.loginNanny = loginNanny;
    }

    public void startButton() {
        switchGUI();
    }

    private void switchGUI() {
        main.setTitle("voting");
        VotingNanny votingNanny = new VotingNanny(main);
        VotingPanel votingPanel = new VotingPanel(votingNanny);

        main.setContentPane(votingPanel);
        main.setSize(800, 600);
        main.setLocationRelativeTo(null);
        main.revalidate();
        main.repaint();
    }
}
