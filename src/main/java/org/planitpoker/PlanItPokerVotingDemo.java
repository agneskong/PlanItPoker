package org.planitpoker;

import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

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

        T12Blackboard.addStory(new T12Story("Build login screen"));
        T12Blackboard.addStory(new T12Story("Create dashboard page"));
        T12Blackboard.addStory(new T12Story("Implement voting backend"));

        LinkedList<T12Story> stories = T12Blackboard.getStories();


        if (stories.isEmpty()) {
            T12Logger.getLogger().warn("No stories found. Please create stories first.");
            return;
        }

        int currentStoryIndex = 0;

        while (currentStoryIndex < stories.size()) {
            T12Story currentStory = stories.get(currentStoryIndex);

            T12Logger.getLogger().info("\n===========================");
            T12Logger.getLogger().info("🧩 Voting on Story: " + currentStory.getTitle());
            T12Logger.getLogger().info("===========================");

            while (true) {
                T12Logger.getLogger().info("\nEnter your name to vote (or type 'done' to finish voting):");
                String name = scanner.nextLine().trim();

                if (name.equals("done")) { 
                    break;
                }

                T12Logger.getLogger().info("Choose your vote (0, ½, 1, 2, 3, 5, 8, 13, 20, 40, 100):");
                String voteInput = scanner.nextLine().trim();

                int voteValue;
                try {
                    if (voteInput.equals("½")) {
                        voteValue = 1; 
                    } else {
                        voteValue = Integer.parseInt(voteInput);
                    }
                } catch (NumberFormatException e) {
                    T12Logger.getLogger().warn("Invalid vote. Try again.");
                    continue;
                }

                currentStory.submitVotes(name, voteValue);
                T12Logger.getLogger().debug(name + " submitted a vote. (Overwrites previous if already voted.)");

            }

            T12Logger.getLogger().info("\n🎉 Revealing votes for story: " + currentStory.getTitle());

            Map<String, Integer> votes = currentStory.getVotes();
            for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                T12Logger.getLogger().info(entry.getKey() + " voted: " + entry.getValue());
            }

            T12Logger.getLogger().info(String.format("📊 Average vote: %.2f", currentStory.calculateAverage()));

            currentStoryIndex++;
        }


        T12Logger.getLogger().info("\n🏁 All stories have been estimated. Great job!");
    }
}

