import model.LogEntry;
import model.LogLevel;
import org.junit.jupiter.api.Test;
import parser.LogEntryParser;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogEntryParserTest {

    @Test
    void shouldParseDateTimeFromLog() {
        LocalDateTime timestamp = LogEntryParser.parseDateTimeFromLog("2026-04-28","09:10:30");
        assertEquals(LocalDateTime.of(2026, 4, 28, 9, 10, 30), timestamp);
    }

    @Test
    void shouldParseErrorLogLine() {
        String line = "2026-04-28 09:09:55 ERROR Database connection lost";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals(LogLevel.ERROR, logEntry.getLevel());
    }

    @Test
    void shouldParseWarningLogLine() {
        String line = "2026-04-28 09:05:33 WARN API response time exceeded threshold";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals(LogLevel.WARN, logEntry.getLevel());
    }

    @Test
    void shouldParseInfoLogLine() {
        String line = "2026-04-28 09:04:10 INFO User login successful";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals(LogLevel.INFO, logEntry.getLevel());
    }

    @Test
    void shouldParseUnknownLogLine() {
        String line = "2026-04-28 09:04:10 BANANA User login successful";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals(LogLevel.UNKNOWN, logEntry.getLevel());
    }

    @Test
    void shouldParseFullLogEntry() {
        String line = "2026-04-28 09:06:45 ERROR Failed to process payment request";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals("Failed to process payment request", logEntry.getMessage());
        assertEquals(LogLevel.ERROR, logEntry.getLevel());
        assertEquals(LocalDateTime.of(2026, 4, 28, 9, 6,45), logEntry.getTimestamp());
        assertEquals(line, logEntry.getRawLine());
    }

    @Test
    void shouldParseFullLogEntryWithIncorrectLevel() {
        String line = "2026-04-28 09:06:45 BANANA Failed to process payment request";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals("Valid log input not received.", logEntry.getMessage());
        assertEquals(LogLevel.UNKNOWN, logEntry.getLevel());
        assertEquals(LocalDateTime.of(2026, 4, 28, 9, 6,45), logEntry.getTimestamp());
        assertEquals(line, logEntry.getRawLine());
    }

    @Test
    void shouldParseFullLogEntryWithIncorrectDate() {
        String line = "bad-date 09:06:45 ERROR Failed to process payment request";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals("Valid log input not received.", logEntry.getMessage());
        assertEquals(LogLevel.UNKNOWN, logEntry.getLevel());
        assertEquals(LocalDateTime.MAX, logEntry.getTimestamp());
        assertEquals(line, logEntry.getRawLine());
    }

    @Test
    void shouldReturnUnknownLogEntryWhenLineIsBlank()  {
        String line = "";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals("Valid log input not received.", logEntry.getMessage());
        assertEquals(LogLevel.UNKNOWN, logEntry.getLevel());
        assertEquals(LocalDateTime.MAX, logEntry.getTimestamp());
        assertEquals(line, logEntry.getRawLine());
    }

    @Test
    void shouldReturnUnknownLogEntryWhenLineHasTooFewParts() {
        String line = "TESTING ERROR ONE";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals("Valid log input not received.", logEntry.getMessage());
        assertEquals(LogLevel.UNKNOWN, logEntry.getLevel());
        assertEquals(LocalDateTime.MAX, logEntry.getTimestamp());
        assertEquals(line, logEntry.getRawLine());
    }

    @Test
    void shouldReturnUnknownLogEntryWhenMessageIsMissing() {
        String line = "2026-04-28 09:06:45 ERROR";
        LogEntry logEntry = LogEntryParser.parseLog(line);

        assertEquals("Valid log input not received.", logEntry.getMessage());
        assertEquals(LogLevel.UNKNOWN, logEntry.getLevel());
        assertEquals(LocalDateTime.MAX, logEntry.getTimestamp());
        assertEquals(line, logEntry.getRawLine());
    }
}
