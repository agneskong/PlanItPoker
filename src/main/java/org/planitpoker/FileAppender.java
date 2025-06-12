package org.planitpoker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * FileAppender is a logging utility class in the PlanitPoker application that implements
 * the Appender interface. It writes log messages to a file named "planitpoker.log".
 * Each log entry is prefixed with a log level (e.g., INFO, ERROR).
 *
 * This class is used to persist application logs for debugging or audit purposes.
 * If writing to the file fails, it reports the error to the standard error stream.
 *
 * Author: Sathvik Chilakala
 * Date: June 12, 2025
 */

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