package org.planitpoker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileAppender implements Appender {
    private static final String LOG_FILE = "planitpoker.log";

    @Override
    public void append(String level, String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println("[" + level + "] " + message);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to write to log file: " + e.getMessage());
        }
    }
} 