package org.planitpoker;

/**
 * The  class is an implementation of the {@link Appender} interface
 * that outputs log messages to the system console. Messages are prefixed with a log level tag.
 * This class is typically used for debugging or monitoring application behavior via the terminal.
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public class ConsoleAppender implements Appender {
    @Override
    public void append(String level, String message) {
        System.out.println("[" + level + "] " + message);
    }
} 