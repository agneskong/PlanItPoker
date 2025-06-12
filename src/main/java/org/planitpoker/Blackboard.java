package org.planitpoker;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class Blackboard {

    private static LinkedList<String> names = new LinkedList<>();
    private static LinkedList<Story> stories = new LinkedList<>();
    private static String currentRoom;
    private static String mode;
    private static String authToken;
    private static final Map<String, Integer> storyTitleToId = new HashMap<>();

    public static synchronized void addName(String name) {
        if (!names.contains(name)) {
            names.add(name);
        }
    }

    public static synchronized LinkedList<String> getNames() {
        return new LinkedList<>(names);
    }

    public static void addStory(Story story) {
        boolean exists = stories.stream().anyMatch(s -> s.getTitle().equals(story.getTitle()));
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

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void mapStory(String title, int id) {
        storyTitleToId.put(title, id);
    }

    public static int getStoryId(String key) {
        return storyTitleToId.getOrDefault(key, -1);
    }

    public static void logoutCurrentUser() {
        if (!names.isEmpty()) names.removeLast();
        currentRoom = null;
        mode = null;
    }
}

