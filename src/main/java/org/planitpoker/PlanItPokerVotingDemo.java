package org.planitpoker;

import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import org.planitpoker.Logger;

/**
 * PlanItPokerVotingDemo
 *
 * This demo allows participants to submit votes for the current story,
 * supporting vote selection, revoting (overwriting previous votes),
 * linking votes to participants, and storing votes in memory.
 * The logic is demonstrated in the console.
 *
 *
 * Satisfies:
 * - Story #41: As a team member, submit my vote from the current story
 * - Task #67: Vote selection logic
 * - Task #69: Allow vote change but prevent multiple votes
 * - Task #70: Store vote in session memory
 * - Task #68: Link vote to the current participant
 *
 * @author agneskong
 */

public class PlanItPokerVotingDemo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Blackboard.addStory(new Story("Build login screen"));
        Blackboard.addStory(new Story("Create dashboard page"));
        Blackboard.addStory(new Story("Implement voting backend"));

        LinkedList<Story> stories = Blackboard.getStories();


        if (stories.isEmpty()) {
            Logger.getLogger().warn("No stories found. Please create stories first.");
            return;
        }

        int currentStoryIndex = 0;

        while (currentStoryIndex < stories.size()) {
            Story currentStory = stories.get(currentStoryIndex);

            Logger.getLogger().info("\n===========================");
            Logger.getLogger().info("🧩 Voting on Story: " + currentStory.getTitle());
            Logger.getLogger().info("===========================");

            while (true) {
                Logger.getLogger().info("\nEnter your name to vote (or type 'done' to finish voting):");
                String name = scanner.nextLine().trim();

                if (name.equals("done")) { 
                    break;
                }

                Logger.getLogger().info("Choose your vote (0, ½, 1, 2, 3, 5, 8, 13, 20, 40, 100):");
                String voteInput = scanner.nextLine().trim();

                int voteValue;
                try {
                    if (voteInput.equals("½")) {
                        voteValue = 1; 
                    } else {
                        voteValue = Integer.parseInt(voteInput);
                    }
                } catch (NumberFormatException e) {
                    Logger.getLogger().warn("Invalid vote. Try again.");
                    continue;
                }

                currentStory.submitVotes(name, voteValue);
                Logger.getLogger().debug(name + " submitted a vote. (Overwrites previous if already voted.)");

            }

            Logger.getLogger().info("\n🎉 Revealing votes for story: " + currentStory.getTitle());

            Map<String, Integer> votes = currentStory.getVotes();
            for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                Logger.getLogger().info(entry.getKey() + " voted: " + entry.getValue());
            }

            Logger.getLogger().info(String.format("📊 Average vote: %.2f", currentStory.calculateAverage()));

            currentStoryIndex++;
        }


        Logger.getLogger().info("\n🏁 All stories have been estimated. Great job!");
    }
}

