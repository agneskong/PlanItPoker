package org.planitpoker;

import java.util.HashMap;
import java.util.Map;


/**
 * Story is a single estimation, added helpers to complete the distrubtedeventhandler
 * voting results, and status
 *
 * @author Sathvik Chilakala
 */


public class Story {
    private final String title;
    private Map<String, Integer> votes;
    private boolean active; 

    public Story(String title) {
        this.title = title;
        this.active = true;
        this.votes = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public boolean isActive() {
        return active;
    }

    public void markCompleted() {
        this.active = false;
    }

    public void submitVotes(String user, int vote) {
        votes.put(user, vote);
    }

    public Map<String, Integer> getVotes() {
        return votes;
    }

    public void clearVotes() {
        votes.clear();
    }

    public double calculateAverage() {
        if (votes.isEmpty()) return 0.0;
        return votes.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
}
