package org.planitpoker;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static final List<Appender> appenders = new ArrayList<>();
    private static final Logger instance = new Logger();

    public static Logger getLogger() {
        return instance;
    }

    public static void addAppender(Appender appender) {
        appenders.add(appender);
    }

    private void log(String level, String message) {
        for (Appender appender : appenders) {
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