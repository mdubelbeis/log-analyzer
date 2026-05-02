package service;

import model.LogEntry;
import model.LogLevel;
import model.LogSummary;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogAnalyzerServiceTest {

    @Test
    void shouldSummarizeLogEntriesByLevel() {
        List<LogEntry> entries = List.of(
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 0, 1),
                        LogLevel.INFO,
                        "Application started",
                        "2026-04-28 09:00:01 INFO Application started"
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 0, 5),
                        LogLevel.INFO,
                        "Database connection established",
                        "2026-04-28 09:00:05 INFO Database connection established"
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 2, 14),
                        LogLevel.WARN,
                        "Slow database query detected",
                        "2026-04-28 09:02:14 WARN Slow database query detected"
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 3, 22),
                        LogLevel.ERROR,
                        "NullPointerException in UserService",
                        "2026-04-28 09:03:22 ERROR NullPointerException in UserService"
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 4, 10),
                        LogLevel.INFO,
                        "User login successful",
                        "2026-04-28 09:04:10 INFO User login successful"
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 5, 33),
                        LogLevel.WARN,
                        "API response time exceeded threshold",
                        "2026-04-28 09:05:33 WARN API response time exceeded threshold"
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 6, 45),
                        LogLevel.ERROR,
                        "Failed to process payment request",
                        "2026-04-28 09:06:45 ERROR Failed to process payment request"
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 8, 12),
                        LogLevel.INFO,
                        "Scheduled job completed",
                        "2026-04-28 09:08:12 INFO Scheduled job completed"
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 9, 55),
                        LogLevel.ERROR,
                        "Database connection lost",
                        "2026-04-28 09:09:55 ERROR Database connection lost"
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 10, 30),
                        LogLevel.INFO,
                        "Application shutdown requested",
                        "2026-04-28 09:10:30 INFO Application shutdown requested"
                )
        );

        LogSummary actualLogSummary = new LogAnalyzerService().summarize(entries);

        assertEquals(10, actualLogSummary.getTotalCount());
        assertEquals(5, actualLogSummary.getInfoCount());
        assertEquals(2, actualLogSummary.getWarnCount());
        assertEquals(3, actualLogSummary.getErrorCount());
        assertEquals(0, actualLogSummary.getUnknownCount());
    }
}
