package org.planitpoker;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a story in the PlanItPoker application for estimation and voting.
 *
 * Each Story has a title, a map of votes by users, and an active status indicating
 * whether voting is still open. Provides methods to submit votes, mark completion,
 * clear votes, and calculate the average vote.
 *
 * Author: Agnes Kong
 * Date: June 12, 2025
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
