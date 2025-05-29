package org.planitpoker;
/**
 * Controller responsible for managing the voting and its interactions.
 *
 * @author agneskong
 */
public class VotingNanny {
    private Main main;
    public VotingNanny(Main main) {

    }
    private void switchGUI() {
        main.setTitle("Room");
        CreateRoomNanny createRoomNanny = new CreateRoomNanny(main);
        CreateRoomPanel createRoomPanel = new CreateRoomPanel(createRoomNanny);
        main.setContentPane(createRoomPanel);
        main.setSize(500, 500);
        main.revalidate();
        main.repaint();
    }
}
