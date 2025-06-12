package org.planitpoker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.*;

/**
 * This class provides utility methods to interact with the Taiga REST API.
 * It supports authenticating users, retrieving project IDs by slug,
 * fetching user stories for projects, and obtaining detailed information about
 * stories and projects from Taiga.
 *
 * It is designed to facilitate integration with Taiga for project and
 * story management, allowing other components to programmatically
 * access and process project backlog data.
 *
 * Author: Agnes Kong
 * Date: June 12, 2025
 */

public class TaigaStoryFetcher {
	
	private static final String TAIGA_API = "https://api.taiga.io/api/v1";

	/**
	public static void main(String[] args) throws Exception {
		try {
			String authToken = loginAndGetToken(USERNAME, PASSWORD);
			// System.out.println("Authenticated successfully. Taiga Token: " + authToken);
			int projectId = getProjectId(authToken, "2thesimplexity-pac-man");
		  //	getUserStories(authToken, projectId);
			System.out.println(projectId);
			// 1. Get all user stories
			JSONArray stories = fetchUserStories(authToken, projectId);						
			// updateBacklogTotalPoints(authToken, stories, 5.0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 **/

	public static String loginAndGetToken(String username, String password) throws IOException {
		URL url = new URL("https://api.taiga.io/api/v1/auth");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);

		JSONObject credentials = new JSONObject();
		credentials.put("type", "normal");
		credentials.put("username", username);
		credentials.put("password", password);

		OutputStream os = conn.getOutputStream();
		os.write(credentials.toString().getBytes(StandardCharsets.UTF_8));
		os.flush();
		os.close();

		int status = conn.getResponseCode();
		System.out.println("🔧 HTTP Status: " + status);

		InputStream responseStream = (status >= 400) ? conn.getErrorStream() : conn.getInputStream();

		BufferedReader in = new BufferedReader(new InputStreamReader(responseStream));
		String response = in.lines().collect(Collectors.joining());
		in.close();

		System.out.println("🔍 Raw response:\n" + response);

		if (status >= 400) {
			throw new IOException("Login failed: " + response);
		}

		JSONObject json = new JSONObject(response);
		return json.getString("auth_token");
	}




	public static int getProjectId(String token, String projectSlug) throws Exception {
		URL url = new URL("https://api.taiga.io/api/v1/projects/by_slug?slug=" + projectSlug);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "Bearer " + token);
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder response = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		reader.close();
		JSONObject json = new JSONObject(response.toString());
		return json.getInt("id");
	}
	
	public static JSONArray fetchUserStories(String token, int projectId) throws Exception {
		URL url = new URL("https://api.taiga.io/api/v1/userstories?project=" + projectId);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "Bearer " + token);		
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder response = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		reader.close();		
		JSONArray allStories = new JSONArray(response.toString());
		JSONArray backlogStories = new JSONArray();
		Map<String, String> roleIdToName = Map.of(
			"5100817", "UX",
			"5100818", "Design",
			"5100816", "Front",
			"5100815", "Back"
		);		
		Map<Integer, Integer> pointIdToValue = Map.of(
			10136072, 8,  // UX
			10136073, 1,  // Design
			10136071, 2,  // Front
			10136075, 3   // Back
		);		
		System.out.println("Backlog stories:");
		for (int i = 0; i < allStories.length(); i++) {
			JSONObject story = allStories.getJSONObject(i);
			if (story.isNull("milestone")) {
				backlogStories.put(story);				
				int id = story.getInt("id");
				String titleKey = "#" + id;
				Blackboard.mapStory(titleKey, id);
				int storyId = Blackboard.getStoryId("#7960399");
				System.out.println(storyId);
				String subject = story.optString("subject", "(no title)");				
				String responsible = "Unassigned";
				if (!story.isNull("assigned_to_extra_info")) {
					responsible = story.getJSONObject("assigned_to_extra_info")
						.optString("full_name_display", "Unassigned");
				}				
				String totalPoints = story.isNull("total_points")
					? "—"
					: String.valueOf(story.getDouble("total_points"));				
				System.out.printf("• #%d - %s\n   Responsible: %s\n   Total Points: %s\n",
					id, subject, responsible, totalPoints);				
				if (!story.isNull("points")) {
					JSONObject pointsObj = story.getJSONObject("points");
					int sum = 0;					
					for (String roleId : pointsObj.keySet()) {
						int pointId = pointsObj.getInt(roleId);
						int value = pointIdToValue.getOrDefault(pointId, -1);
						String role = roleIdToName.getOrDefault(roleId, "Unknown");
						
						System.out.printf("     - %s (roleId: %s) → pointId: %d → value: %s\n",
							role, roleId, pointId, (value >= 0 ? value : "?"));
						if (value >= 0) sum += value;
					}				
					System.out.println("     = Computed Sum: " + sum + "\n");
				} else {
					System.out.println("     No per-role points assigned.\n");
				}
			}
		}		
		return backlogStories;
	}
	/*
	public static void updateBacklogTotalPoints(String token, int storyId, int points) throws IOException {
		URL url = new URL(TAIGA_API + "/userstories/" + storyId);
		JSONObject story = getStoryDetails(token, storyId);
		int version = story.getInt("version");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST"); // PATCH override
		conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);

		JSONObject update = new JSONObject();
		update.put("total_points", points);
		update.put("points", new JSONObject());
		update.put("version", version);

		OutputStream os = conn.getOutputStream();
		os.write(update.toString().getBytes(StandardCharsets.UTF_8));
		os.flush();
		os.close();

		int status = conn.getResponseCode();
		if (status >= 400) {
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			String error = errorReader.lines().collect(Collectors.joining());
			errorReader.close();
			throw new IOException("Failed to update story: " + error);
		}

		System.out.println("✅ Story #" + storyId + " updated to " + points + " points");
	}
	*/

	public static JSONObject getStoryDetails(String token, int storyId) throws IOException {
		URL url = new URL("https://api.taiga.io/api/v1/userstories/" + storyId);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "Bearer " + token);
		conn.setRequestMethod("GET");

		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String response = reader.lines().collect(Collectors.joining());
		reader.close();

		JSONObject story = new JSONObject(response);
		System.out.println("📘 Story Details:\n" + story.toString(2)); // Pretty-print
		return story;
	}

	public static JSONObject getProjectInfo(String token, int projectId) throws IOException {
		URL url = new URL("https://api.taiga.io/api/v1/projects/" + projectId);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Authorization", "Bearer " + token);
		conn.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String response = in.lines().collect(Collectors.joining());
		in.close();

		JSONObject json = new JSONObject(response);
		System.out.println("📦 Project Info:\n" + json.toString(2));
		return json;
	}
}
