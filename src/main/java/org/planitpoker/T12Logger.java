package org.planitpoker;

import java.util.ArrayList;
import java.util.List;

/**
 * Logger is a singleton logging utility for the PlanitPoker application.
 * It supports multiple log levels including TRACE, DEBUG, INFO, WARN, and ERROR.
 * Log messages are dispatched to all registered Appenders, such as ConsoleAppender
 * and FileAppender, which handle the output format and destination.
 *
 * Usage:
 * - Call Logger.getLogger() to access the global logger instance.
 * - Use addAppender() to register output targets.
 * - Use level-specific methods like info(), debug(), or error() to log messages.
 *
 * This flexible design allows for modular logging to multiple outputs.
 *
 * Author: Sathvik Chilakala and Justin Diaz
 * Date: June 12, 2025
 */


public class T12Logger {
    private static final List<T12Appender> appenders = new ArrayList<>();
    private static final T12Logger instance = new T12Logger();

    public static T12Logger getLogger() {
        return instance;
    }

    public static void addAppender(T12Appender appender) {
        appenders.add(appender);
    }

    private void log(String level, String message) {
        for (T12Appender appender : appenders) {
            appender.append(level, message);
        }
    }

    public void trace(String message) {
        log("TRACE", message);
    }
    public void debug(String message) {
        log("DEBUG", message);
    }
    public void info(String message) {
        log("INFO", message);
    }
    public void warn(String message) {
        log("WARN", message);
    }
    public void error(String message) {
        log("ERROR", message);
    }
} 