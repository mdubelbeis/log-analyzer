package service;

import model.LogEntry;
import model.LogLevel;
import model.LogSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogAnalyzerServiceTest {

    private List<LogEntry> entries;
    private List<LogEntry> errorResults;
    private List<LogEntry> warnResults;
    private List<LogEntry> infoResults;
    private List<LogEntry> unknownResults;
    private LogAnalyzerService logAnalyzerService;

    private List<LogEntry> emptyEntries;

    @BeforeEach
    void setUp() {
        entries = List.of(
                createEntry(9,0,1, LogLevel.INFO, "Application started"),
                createEntry(9,0,5, LogLevel.INFO, "Database connection established"),
                createEntry(9,2,14, LogLevel.WARN, "Slow database query detected"),
                createEntry(9,3,22, LogLevel.ERROR, "NullPointerException in UserService"),
                createEntry(9,4,10, LogLevel.INFO, "User login successful"),
                createEntry(9,5,33, LogLevel.WARN, "API response time exceeded threshold"),
                createEntry(9,6,45, LogLevel.ERROR, "Failed to process payment request"),
                createEntry(9,8,12, LogLevel.INFO, "Scheduled job completed"),
                createEntry(9,9,55, LogLevel.ERROR, "Database connection lost"),
                createEntry(9,10,30, LogLevel.INFO, "Application shutdown requested"),
                createEntry(9,10,33, LogLevel.UNKNOWN, "")
        );

        logAnalyzerService = new LogAnalyzerService();
        errorResults = logAnalyzerService.filterByLevel(entries, LogLevel.ERROR);
        warnResults = logAnalyzerService.filterByLevel(entries, LogLevel.WARN);
        infoResults = logAnalyzerService.filterByLevel(entries, LogLevel.INFO);
        unknownResults = logAnalyzerService.filterByLevel(entries, LogLevel.UNKNOWN);

        // Edge-Case
        emptyEntries = List.of();
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
    void shouldReturnZeroSummaryForEmptyEntries() {
        LogSummary actualLogSummary = logAnalyzerService.summarize(emptyEntries);
        assertEquals(0, actualLogSummary.getTotalCount());
        assertEquals(0, actualLogSummary.getInfoCount());
        assertEquals(0, actualLogSummary.getWarnCount());
        assertEquals(0, actualLogSummary.getErrorCount());
        assertEquals(0, actualLogSummary.getUnknownCount());
    }

    @Test
    void shouldFilterErrorEntriesByLevel() {
        assertEquals(3, errorResults.size());

        for (LogEntry entry: errorResults) {
            assertEquals(LogLevel.ERROR, entry.getLevel());
        }
    }

    @Test
    void shouldReturnEmptyListWhenFilteringEmptyEntries() {
        List<LogEntry> errorResults = logAnalyzerService.filterByLevel(emptyEntries, LogLevel.ERROR);
        List<LogEntry> warnResults = logAnalyzerService.filterByLevel(emptyEntries, LogLevel.WARN);
        List<LogEntry> infoResults = logAnalyzerService.filterByLevel(emptyEntries, LogLevel.INFO);
        List<LogEntry> unknownResults = logAnalyzerService.filterByLevel(emptyEntries, LogLevel.UNKNOWN);

        assertTrue(errorResults.isEmpty());
        assertTrue(warnResults.isEmpty());
        assertTrue(infoResults.isEmpty());
        assertTrue(unknownResults.isEmpty());
    }

    @Test
    void shouldFilterWarnEntriesByLevel() {
        assertEquals(2, warnResults.size());

        for(LogEntry entry: warnResults) {
            assertEquals(LogLevel.WARN, entry.getLevel());
        }
    }

    @Test
    void shouldReturnEmptyListWhenSearchingEmptyEntries() {
        List<LogEntry> searchResults =logAnalyzerService.search(emptyEntries, "database");

        assertTrue(searchResults.isEmpty());
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

    @Test
    void shouldSearchLogEntriesByKeyword() {
        List<LogEntry> results = logAnalyzerService.search(entries, "database");

        assertEquals(3, results.size());

        for (LogEntry result: results) {
            assertTrue(result.getRawLine().toLowerCase().contains("database"));
        }
    }

    private LogEntry createEntry(int hour, int minute, int second, LogLevel level, String message) {
        LocalDateTime timestamp = LocalDateTime.of(2026, 4, 28, hour, minute, second);

        String rawLine = String.format(
                "2026-04-28 %02d:%02d:%02d %s %s",
                hour,
                minute,
                second,
                level,
                message
        );

        return new LogEntry(timestamp, level, message, rawLine);
    }

}
