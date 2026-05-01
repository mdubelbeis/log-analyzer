package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class LogEntryTest {
    private LogEntry logEntry;

    @BeforeEach
    void setUp() {
        String timestamp = "2026-04-28 09:10:30";
        String message = "Application shutdown requested";
        String rawLine = "2026-04-28 09:10:30 ERROR Application shutdown requested";

        logEntry = new LogEntry(timestamp, LogLevel.ERROR, message , rawLine);
    }

    @Test
    void shouldCreateLogEntryWithLevelAndMessage() {
        assertEquals(LogLevel.ERROR, logEntry.getLevel());
        assertEquals("Application shutdown requested", logEntry.getMessage());
    }

    @Test
    void shouldCreateLogEntryWithRawLine() {
        assertEquals("2026-04-28 09:10:30 ERROR Application shutdown requested", logEntry.getRawLine());
    }

    @Test
    void shouldCreateLogEntryWithTimestamp() {
        assertEquals("2026-04-28 09:10:30", logEntry.getTimestamp());
    }
}
