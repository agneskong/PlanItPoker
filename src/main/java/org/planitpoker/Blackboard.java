package org.planitpoker;

import java.util.LinkedList;


/**
 * Blackboard is a shared data source for all the other components,
 * voting, and game status
 *
 * @author Sathvik Chilakala
 */


public class Blackboard {

    private static LinkedList<String> names = new LinkedList<>();
    private static LinkedList<Story> stories = new LinkedList<>();
    private static String currentRoom;
    private static String mode;

    public static void addName(String name) {
        if (!names.contains(name)) {
            names.add(name);
        }
    }

    public static LinkedList<String> getNames() {
        return names;
    }

    public static void addStory(Story story) {
        boolean exists = false;
        for (Story s : stories) {
            if (s.getTitle().equals(story.getTitle())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            stories.add(story);
        }
    }

    public static LinkedList<Story> getStories() {
        return stories;
    }

    public static void addCurrentRoom(String name) {
        currentRoom = name;
    }

    public static String getCurrentRoom() {
        return currentRoom;
    }

    public static void addCurrentMode(String selectedItem) {
        mode = selectedItem;
    }

    public static String getMode() {
        return mode;
    }

    public static void logoutCurrentUser() {
        if (!names.isEmpty()) names.removeLast();
        currentRoom = null;
        mode = null;
    }
}
