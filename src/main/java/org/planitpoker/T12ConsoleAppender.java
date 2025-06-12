package org.planitpoker;

/**
 * The  class is an implementation of the interface
 * that outputs log messages to the system console. Messages are prefixed with a log level tag.
 * This class is typically used for debugging or monitoring application behavior via the terminal.
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public class T12ConsoleAppender implements T12Appender {
    @Override
    public void append(String level, String message) {
        System.out.println("[" + level + "] " + message);
    }
} 