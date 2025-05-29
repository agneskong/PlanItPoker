package org.planitpoker;

import java.util.LinkedList;

/**
 * Shared data structure for the application.
 *
 * @author javiergs
 */
public class Blackboard {

	private static LinkedList<String> names = new LinkedList<>();
	private static LinkedList<Story> stories = new LinkedList<>();
	private static String currentRoom;
	private static String mode;


	// Agnes Kong

	// vars

	public static void addName(String name) {
		names.add(name);
	}

	public static String getCurrentRoom() {
		return currentRoom;
	}

	public static void addStory(Story story) {
		stories.add(story);
	}

	public static void addCurrentRoom(String name) {
		currentRoom = name;
	}

	public static void addCurrentMode(String selectedItem) {
		mode = selectedItem;
	}

	public static LinkedList<Story> getStories() {
		return stories;
	}

}
