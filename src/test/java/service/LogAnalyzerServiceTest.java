package service;

import model.LogEntry;
import model.LogLevel;
import model.LogSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogAnalyzerServiceTest {

    private List<LogEntry> entries;
    private List<LogEntry> errorResults;
    private List<LogEntry> warnResults;
    private List<LogEntry> infoResults;
    private List<LogEntry> unknownResults;
    private LogAnalyzerService logAnalyzerService;

    @BeforeEach
    void setUp() {
        entries = List.of(
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
                ),
                new LogEntry(
                        LocalDateTime.of(2026, 4, 28, 9, 10, 30),
                        LogLevel.UNKNOWN,
                        "",
                        ""
                )
        );
        logAnalyzerService = new LogAnalyzerService();
        errorResults = logAnalyzerService.filterByLevel(entries, LogLevel.ERROR);
        warnResults = logAnalyzerService.filterByLevel(entries, LogLevel.WARN);
        infoResults = logAnalyzerService.filterByLevel(entries, LogLevel.INFO);
        unknownResults = logAnalyzerService.filterByLevel(entries, LogLevel.UNKNOWN);

    }

    @Test
    void shouldSummarizeLogEntriesByLevel() {
        LogSummary actualLogSummary = logAnalyzerService.summarize(entries);

        assertEquals(11, actualLogSummary.getTotalCount());
        assertEquals(5, actualLogSummary.getInfoCount());
        assertEquals(2, actualLogSummary.getWarnCount());
        assertEquals(3, actualLogSummary.getErrorCount());
        assertEquals(1, actualLogSummary.getUnknownCount());
    }

    @Test
    void shouldFilterErrorEntriesByLevel() {
        assertEquals(3, errorResults.size());

        for (LogEntry entry: errorResults) {
            assertEquals(LogLevel.ERROR, entry.getLevel());
        }
    }

    @Test
    void shouldFilterWarnEntriesByLevel() {
        assertEquals(2, warnResults.size());

        for(LogEntry entry: warnResults) {
            assertEquals(LogLevel.WARN, entry.getLevel());
        }
    }

    @Test
    void shouldFilterInfoEntriesByLevel() {
        assertEquals(5, infoResults.size());

        for (LogEntry entry: infoResults) {
            assertEquals(LogLevel.INFO, entry.getLevel());
        }
    }

    @Test
    void shouldFilterUnknownEntriesByLevel() {
        assertEquals(1, unknownResults.size());

        for (LogEntry entry: unknownResults) {
            assertEquals(LogLevel.UNKNOWN, entry.getLevel());
        }

    }
}
