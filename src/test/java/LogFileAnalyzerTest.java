import model.LogEntry;
import model.LogLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LogFileAnalyzerTest {

    @TempDir
    private Path tempDir;

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream output;
    private Path logFile;
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
        logFile = tempDir.resolve("test.log");

        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
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
        Files.write(logFile, lines);

        List<String> results = LogFileAnalyzer.readAllLines(logFile.toString());
        assertEquals(11, results.size());
        assertEquals("2026-04-28 09:00:01 INFO Application started", results.getFirst());
        assertEquals("2026-04-28 09:11:33 UNKNOWN Application shutdown requested", results.getLast());
    }

    @Test
    void shouldReturnTrueWhenFilePathIsValid() throws IOException {
        Files.write(logFile, lines);

        assertTrue(LogFileAnalyzer.isValidFilePath(logFile.toString()));
    }

    @Test
    void shouldReturnFalseWhenFileDoesNotExist() {
        assertFalse(LogFileAnalyzer.isValidFilePath(logFile.toString()));
    }

    @Test
    void shouldReturnFalseWhenPathIsDirectory() {
        assertFalse(LogFileAnalyzer.isValidFilePath(tempDir.toString()));
    }

    @Test
    void shouldDisplayHelp() {
        LogFileAnalyzer.displayHelp();
        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool help"));
        assertTrue(printedOutput.contains("\tlogtool summary <file>"));
        assertTrue(printedOutput.contains("\tlogtool errors <file>"));
        assertTrue(printedOutput.contains("\tlogtool warnings <file>"));
        assertTrue(printedOutput.contains("\tlogtool search <keyword> <file>"));
    }

    @Test
    void shouldPrintMissingFileMessageForSummaryUsage() {
        LogFileAnalyzer.printSummaryUsage(new String[]{"summary"});

        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Missing file path."));
        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool summary <file>"));
    }

    @Test
    void shouldPrintTooManyArgumentsMessageForSummaryUsage() {
        LogFileAnalyzer.printSummaryUsage(new String[]{"summary", "file.log", "extra"});

        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Too many arguments passed."));
        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool summary <file>"));
    }

    @Test
    void shouldPrintMissingFileMessageForErrorsUsage() {
        LogFileAnalyzer.printErrorsUsage(new String[]{"errors"});

        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Missing file path."));
        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool errors <file>"));
    }

    @Test
    void shouldPrintTooManyArgumentsMessageForErrorUsage() {
        LogFileAnalyzer.printErrorsUsage(new String[]{"errors", "file.log", "extra"});

        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Too many arguments passed."));
        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool errors <file>"));
    }

    @Test
    void shouldPrintMissingFileMessageForWarningsUsage() {
        LogFileAnalyzer.printWarningsUsage(new String[]{"warnings"});

        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Missing file path."));
        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool warnings <file>"));
    }

    @Test
    void shouldPrintTooManyArgumentsMessageForWarningsUsage() {
        LogFileAnalyzer.printWarningsUsage(new String[]{"warnings", "file.log", "extra"});

        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Too many arguments passed."));
        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool warnings <file>"));
    }

    @Test
    void shouldPrintMissingKeywordAndFileMessageForSearchUsage() {
        LogFileAnalyzer.printSearchUsage(new String[]{"search"});

        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Missing keyword and file."));
        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool search <keyword> <file>"));
    }

    @Test
    void shouldPrintMissingFileMessageForSearchUsage() {
        LogFileAnalyzer.printSearchUsage(new String[]{"search", "keyword"});

        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Missing file."));
        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool search <keyword> <file>"));
    }

    @Test
    void shouldPrintTooManyArgumentsMessageForSearchUsage() {
        LogFileAnalyzer.printSearchUsage(new String[]{"search", "keyword", "file.text", "extra"});

        String printedOutput = output.toString();

        assertTrue(printedOutput.contains("Too many arguments passed."));
        assertTrue(printedOutput.contains("Usage:"));
        assertTrue(printedOutput.contains("\tlogtool search <keyword> <file>"));
    }
}
