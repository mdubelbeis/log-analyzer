package service;

import model.LogEntry;
import model.LogLevel;
import model.LogSummary;

import java.util.List;

public class LogAnalyzerService {
    public LogSummary summarize(List<LogEntry> entries) {

        int totalCount = entries.size();
        int infoCount = 0;
        int warnCount = 0;
        int errorCount = 0;
        int unknownCount = 0;

        for (LogEntry entry: entries) {
            if (entry.getLevel() == LogLevel.INFO) infoCount += 1;
            if (entry.getLevel() == LogLevel.WARN) warnCount += 1;
            if (entry.getLevel() == LogLevel.ERROR) errorCount += 1;
            if (entry.getLevel() == LogLevel.UNKNOWN) unknownCount += 1;
        }

        return new LogSummary(totalCount, infoCount, warnCount, errorCount, unknownCount);

    }
}
