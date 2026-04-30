package service;

import model.*;

import java.util.ArrayList;
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

    public List<LogEntry> filterByLevel(List<LogEntry> entries, LogLevel level) {
        ArrayList<LogEntry> matches = new ArrayList<>();
        for (LogEntry entry: entries) {
            if (entry.getLevel() == level) {
                matches.add(entry);
            }
        }
        return matches;
    }

    public List<LogEntry> search(List<LogEntry> entries, String keyword) {
        List<LogEntry> matches = new ArrayList<>();
        for (LogEntry entry: entries) {
            if (entry.getRawLine().toLowerCase().contains(keyword)) {
                matches.add(entry);
            }
        }
        return matches;
    }

}
