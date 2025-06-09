package org.planitpoker;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class WestPanel extends JPanel {
    private JLabel userLabel;
    private JTextArea playersArea;
    private DashboardNanny dashboardNanny;
    private Main main;
    private LoginNanny loginNanny;

    public WestPanel(DashboardNanny dashboardNanny, Main main, LoginNanny loginNanny) {
        this.dashboardNanny = dashboardNanny;
        this.main = main;
        this.loginNanny = loginNanny;

        setBackground(new Color(255, 220, 220));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(230, 0));

        userLabel = new JLabel(getCurrentUser(), SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel playersLabel = new JLabel("Players:");
        playersLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        playersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playersArea = new JTextArea();
        playersArea.setEditable(false);
        playersArea.setBackground(new Color(255, 220, 220));
        playersArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        playersArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        playersArea.setMaximumSize(new Dimension(180, 100));

        JLabel timerLabel = new JLabel("00:00:00");
        timerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel inviteLabel = new JLabel("Invite a teammate");
        inviteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField urlField = new JTextField("https://app.planitpoker.com");
        urlField.setEditable(false);
        urlField.setMaximumSize(new Dimension(200, 28));
        urlField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        urlField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton copyButton = new JButton("Copy URL");
        copyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(18));
        add(userLabel);
        add(Box.createVerticalStrut(8));
        add(startButton);
        add(Box.createVerticalStrut(16));
        add(playersLabel);
        add(playersArea);
        add(timerLabel);
        add(Box.createVerticalStrut(10));
        add(inviteLabel);
        add(Box.createVerticalStrut(4));
        add(urlField);
        add(Box.createVerticalStrut(4));
        add(copyButton);
        add(Box.createVerticalStrut(10));
        add(logoutButton);

        startButton.addActionListener(e -> dashboardNanny.startButton());

        copyButton.addActionListener(e -> {
            urlField.selectAll();
            urlField.copy();
            JOptionPane.showMessageDialog(this, "Copied to clipboard!");
        });

        logoutButton.addActionListener(e -> handleLogout());
    }

    private String getCurrentUser() {
        LinkedList<String> names = Blackboard.getNames();
        return names.isEmpty() ? "User" : names.getLast();
    }

    private void refreshPlayerNames() {
        userLabel.setText(getCurrentUser());
        StringBuilder sb = new StringBuilder();
        for (String name : Blackboard.getNames()) {
            sb.append(name).append("\n");
        }
        playersArea.setText(sb.toString());
    }

    private void handleLogout() {
        loginNanny.logout();
    }

    public void refreshStories() {
        refreshPlayerNames();
    }
}
