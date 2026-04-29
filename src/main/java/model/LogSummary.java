package model;

public class LogSummary {
    private final int totalCount;
    private final int infoCount;
    private final int warnCount;
    private final int errorCount;
    private final int unknownCount;

    public LogSummary(int totalCount, int infoCount, int warnCount, int errorCount, int unknownCount) {
        this.totalCount = totalCount;
        this.infoCount = infoCount;
        this.warnCount = warnCount;
        this.errorCount = errorCount;
        this.unknownCount = unknownCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getInfoCount() {
        return infoCount;
    }

    public int getWarnCount() {
        return warnCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getUnknownCount() {
        return unknownCount;
    }
}
