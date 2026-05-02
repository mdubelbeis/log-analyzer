package parser;

import model.LogEntry;
import model.LogLevel;

import java.time.LocalDateTime;

public class LogEntryParser {
    public static LogEntry parseLog(String line) {
        String[] splitLine = line.split(" ", 4);

        if (splitLine.length < 4) {
            return new LogEntry(LocalDateTime.now(), LogLevel.UNKNOWN, line, line);
        }

        LocalDateTime timestamp = parseDateTimeFromLog(splitLine[0], splitLine[1]);
        String message = splitLine[3];

        try {
            LogLevel level = LogLevel.valueOf(splitLine[2].toUpperCase());
            return new LogEntry(timestamp, level, message, line);
        } catch (IllegalArgumentException e) {
            return new LogEntry(timestamp, LogLevel.UNKNOWN, message, line);
        }

    }

    public static LocalDateTime parseDateTimeFromLog(String date, String time) {
        String[] parsedDate = date.split("-");

        int year = Integer.parseInt(parsedDate[0]);
        int month = Integer.parseInt(parsedDate[1]);
        int day = Integer.parseInt(parsedDate[2]);

        String[] parsedTime = time.split(":");

        int hour = Integer.parseInt(parsedTime[0]);
        int minutes = Integer.parseInt(parsedTime[1]);
        int seconds = Integer.parseInt(parsedTime[2]);

        return LocalDateTime.of(year, month, day, hour, minutes, seconds);
    }
}
