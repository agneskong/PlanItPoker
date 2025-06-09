package org.planitpoker;

public class ConsoleAppender implements Appender {
    @Override
    public void append(String level, String message) {
        System.out.println("[" + level + "] " + message);
    }
} 