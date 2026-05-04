import model.LogEntry;
import model.LogLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogFileAnalyzerTest {

    @TempDir
    Path tempDir;
    private List<String> lines;
    private final List<String> emptyLines = List.of();
    private List<LogEntry> results;


    @BeforeEach
    void setUp() {
        lines = List.of(
                "2026-04-28 09:00:01 INFO Application started",
                "2026-04-28 09:00:05 INFO Database connection established",
                "2026-04-28 09:02:14 WARN Slow database query detected",
                "2026-04-28 09:03:22 ERROR NullPointerException in UserService",
                "2026-04-28 09:04:10 INFO User login successful",
                "2026-04-28 09:05:33 WARN API response time exceeded threshold",
                "2026-04-28 09:06:45 ERROR Failed to process payment request",
                "2026-04-28 09:08:12 INFO Scheduled job completed",
                "2026-04-28 09:09:55 ERROR Database connection lost",
                "2026-04-28 09:10:30 INFO Application shutdown requested",
                "2026-04-28 09:11:33 UNKNOWN Application shutdown requested"
        );

        results = LogFileAnalyzer.parseEntries(lines);
    }

    @Test
    void shouldReturnEmptyListWhenNoLinesProvided() {
        assertEquals(0, LogFileAnalyzer.parseEntries(emptyLines).size());
    }

    @Test
    void shouldReturnParsedEntriesForValidLines() {
        assertEquals(11, results.size());
    }

    @Test
    void shouldParseFirstLogEntryWithCorrectFields() {
        LogEntry firstEntry = results.getFirst();

        assertEquals(LogLevel.INFO, firstEntry.getLevel());
        assertEquals(LocalDateTime.of(2026, 4, 28, 9, 0, 1), firstEntry.getTimestamp());
        assertEquals("Application started", firstEntry.getMessage());
        assertEquals("2026-04-28 09:00:01 INFO Application started", firstEntry.getRawLine());
    }

    @Test
    void shouldReadAllLinesFromFile() throws IOException {
        Path logFile = tempDir.resolve("test.log");
        Files.write(logFile, lines);

        List<String> results = LogFileAnalyzer.readAllLines(logFile.toString());
        assertEquals(11, results.size());
        assertEquals("2026-04-28 09:00:01 INFO Application started", results.getFirst());
        assertEquals("2026-04-28 09:11:33 UNKNOWN Application shutdown requested", results.getLast());
    }

}
