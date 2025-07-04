package org.planitpoker;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

/**
 * The class acts as a central storage area (or "blackboard")
 * for managing various static application data including user names, stories,
 * the current room, mode, authentication token, and a mapping of story titles to their IDs.
 *
 * This centralized class facilitates data management and state coordination
 * in the application, enabling methods to add, retrieve, and update the stored information.
 *
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public class T12Blackboard {

    private static LinkedList<String> names = new LinkedList<>();
    private static LinkedList<T12Story> stories = new LinkedList<>();
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

    public static void addStory(T12Story story) {
        boolean exists = stories.stream().anyMatch(s -> s.getTitle().equals(story.getTitle()));
        if (!exists) {
            stories.add(story);
        }
    }

    public static LinkedList<T12Story> getStories() {
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

