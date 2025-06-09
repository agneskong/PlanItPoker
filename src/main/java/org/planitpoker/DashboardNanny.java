package org.planitpoker;

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
