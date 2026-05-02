package model;

import java.time.LocalDateTime;

public class LogEntry {
    private final LocalDateTime timestamp;
    private final LogLevel level;
    private final String message;
    private final String rawLine;

    public LogEntry(LocalDateTime timestamp, LogLevel level, String message, String rawLine) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.rawLine = rawLine;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getRawLine() {
        return rawLine;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp='" + timestamp + '\'' +
                ", level=" + level +
                ", message='" + message + '\'' +
                ", rawLine='" + rawLine + '\'' +
                '}';
    }
}
