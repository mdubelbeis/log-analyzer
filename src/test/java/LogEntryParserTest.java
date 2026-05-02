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
}
