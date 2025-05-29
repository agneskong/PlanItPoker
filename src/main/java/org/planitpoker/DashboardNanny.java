package org.planitpoker;
import org.eclipse.paho.client.mqttv3.MqttCallback;

/**
 * Controller responsible for managing the dashboard and its interactions.
 *
 * @author javiergs
 */
public class DashboardNanny {
    private Main main;

    public DashboardNanny(Main main) {

        this.main = main;
    }

    public void startButton() {
        switchGUI();

    }
    private void switchGUI() {
        main.setTitle("voting");
        VotingNanny votingNanny = new VotingNanny(main);
        VotingPanel votingPanel= new VotingPanel(votingNanny);

        main.setContentPane(votingPanel);
        main.setSize(800, 600);
        main.setLocationRelativeTo(null);
        main.revalidate();
        main.repaint();
    }


}
