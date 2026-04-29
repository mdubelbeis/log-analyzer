package parser;

import model.LogEntry;
import model.LogLevel;

public class LogEntryParser {
    public static LogEntry parseLog(String line) {
        String[] splitLine = line.split(" ", 4);

        if (splitLine.length < 4) {
            return new LogEntry("", LogLevel.UNKNOWN, line, line);
        }

        String timestamp = splitLine[0] + " " + splitLine[1];
        String rawLine = line;
        String message = splitLine[3];

        try {
            LogLevel level = LogLevel.valueOf(splitLine[2].toUpperCase());
            return new LogEntry(timestamp, level, message, rawLine);
        } catch (IllegalArgumentException e) {
            return new LogEntry(timestamp, LogLevel.UNKNOWN, message, rawLine);
        }

    }
}
