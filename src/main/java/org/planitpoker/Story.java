package org.planitpoker;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Float.sum;

/**
 * Represents a story to be estimated in PlanItPoker.
 * Each story has a title, description, and a set of votes from participants.
 *
 * Related to Story #39: As a facilitator, I want to add stories to estimate
 * so the team can vote on them.
 *
 * @author agneskong
 */

public class Story {
    private final String title;
    private Map<String, Integer> votes;
    private boolean status; // true if active, false if completed vote

    public Story(String title) {
        this.title = title;
        this.status = true;
        this.votes = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }
    public boolean getStatus() { return status;}

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
        return votes.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

}
