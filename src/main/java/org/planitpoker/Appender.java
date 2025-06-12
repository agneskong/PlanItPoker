package org.planitpoker;

/**
 * The interface defines a simple logging mechanism
 * that allows messages to be appended with a specified log level.
 * This interface is intended to be implemented by classes that handle
 * logging or message output (e.g., to a console, file, or GUI).
 *
 * Author: Justin Diaz
 * Date: June 12, 2025
 */

public interface Appender {
    void append(String level, String message);
} 