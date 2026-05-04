
import model.*;
import parser.LogEntryParser;
import service.LogAnalyzerService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class LogFileAnalyzer {
    public static void main(String[] args) {
        if (args.length == 0) {
            displayHelp();
            return;
        }

        LogAnalyzerService logAnalyzerService = new LogAnalyzerService();
        String command = args[0];

        switch (command) {
            case "help" -> {
                displayHelp();
            }
            case "summary" -> {
                if (args.length == 2) {
                    String file = args[1];

                    if (isValidFilePath(file)) {
                        handleSummary(file, logAnalyzerService);
                    }
                } else {
                    printSummaryUsage(args);
                }
            }
            case "errors" -> {
                if (args.length == 2) {
                    String file = args[1];

                    if (isValidFilePath(file)) {
                        handleErrors(file, logAnalyzerService);
                    }
                } else {
                    printErrorsUsage(args);
                }
            }
            case "warnings" -> {
                if (args.length == 2) {
                    String file = args[1];

                    if (isValidFilePath(file)) {
                        handleWarnings(file, logAnalyzerService);
                    }
                } else {
                    printWarningsUsage(args);
                }
            }
            case "search" -> {
               if (args.length == 3) {
                   String originalKeyword = args[1];
                   String keyword = originalKeyword.toLowerCase();
                   String file = args[2];

                   if (isValidFilePath(file)) {
                       System.out.println("Processing search command for: " + originalKeyword + " in " + file);
                       handleSearch(file, keyword, logAnalyzerService);
                   }
               } else {
                    printSearchUsage(args);
               }
            }
            default -> {
                System.out.println("Unknown command: " + args[0]);
                System.out.println("Run 'logtool help' to see available commands");
            }
        }

    }

    public static void handleSummary(String file, LogAnalyzerService logAnalyzerService) {
        System.out.println("Processing summary command for: " + file);
        List<LogEntry> entries = loadEntries(file);
        LogSummary summary = logAnalyzerService.summarize(entries);

        printSummary(summary);
    }

    public static void handleErrors(String file, LogAnalyzerService logAnalyzerService) {
        System.out.println("Processing errors command for: " + file);
        List<LogEntry> entries = loadEntries(file);
        List<LogEntry> matches =  logAnalyzerService.filterByLevel(entries, LogLevel.ERROR);

        printLines(matches);
    }

    public static void handleWarnings(String file, LogAnalyzerService logAnalyzerService) {
        System.out.println("Processing warnings command for: " + file);
        List<LogEntry> entries = loadEntries(file);
        List<LogEntry> matches = logAnalyzerService.filterByLevel(entries, LogLevel.WARN);

        printLines(matches);
    }

    public static void handleSearch(String file, String keyword, LogAnalyzerService logAnalyzerService) {
        List<LogEntry> entries = loadEntries(file);
        List<LogEntry> matches = logAnalyzerService.search(entries, keyword);

        printLines(matches);
    }

    public static void printSummaryUsage(String[] args) {
        if (args.length == 1) {
            System.out.println("Missing file path.");
            System.out.println("Usage:");
            System.out.println("\tlogtool summary <file>");
            return;
        }
        System.out.println("Too many arguments passed.");
        System.out.println("Usage:");
        System.out.println("\tlogtool summary <file>");
    }

    public static void printErrorsUsage(String[] args) {
        if (args.length == 1) {
            System.out.println("Missing file path.");
            System.out.println("Usage:");
            System.out.println("\tlogtool errors <file>");
            return;
        }
        System.out.println("Too many arguments passed.");
        System.out.println("Usage:");
        System.out.println("\tlogtool errors <file>");
    }

    public static void printWarningsUsage(String[] args) {
        if (args.length == 1) {
            System.out.println("Missing file path.");
            System.out.println("Usage:");
            System.out.println("\tlogtool warnings <file>");
            return;
        }
        System.out.println("Too many arguments passed.");
        System.out.println("Usage:");
        System.out.println("\tlogtool warnings <file>");
    }

    public static void printSearchUsage(String[] args) {
        if (args.length == 1) {
            System.out.println("Missing keyword and file.");
            System.out.println("Usage:");
            System.out.println("\tlogtool search <keyword> <file>");
            return;
        } else if (args.length == 2) {
            System.out.println("Missing file.");
            System.out.println("Usage:");
            System.out.println("\tlogtool search <keyword> <file>");
            return;
        }
        System.out.println("Too many arguments passed.");
        System.out.println("Usage:");
        System.out.println("\tlogtool search <keyword> <file>");
    }

    public static boolean isValidFilePath(String file) {
        Path path = Path.of(file);

        if (!Files.exists(path)) {
            System.out.println("File not found: " + file);
            return false;
        }

        if (!Files.isRegularFile(path)) {
            System.out.println("Path is not a file: " + file);
            return false;
        }

        if (!Files.isReadable(path)) {
            System.out.println("File is not readable: " + file);
            return false;
        }

        return true;
    }

    public static void displayHelp() {
        System.out.println("Usage:");
        System.out.println("\tlogtool help");
        System.out.println("\tlogtool summary <file>");
        System.out.println("\tlogtool errors <file>");
        System.out.println("\tlogtool warnings <file>");
        System.out.println("\tlogtool search <keyword> <file>");
    }

    public static List<String> readAllLines(String file) {
        try {
            return Files.readAllLines(Path.of(file));
        } catch (IOException e) {
            System.out.println("Could not read file: " + file);
            System.out.println("Reason: " + e.getMessage());
            return List.of();
        }
    }

    public static List<LogEntry> parseEntries(List<String> lines) {
        ArrayList<LogEntry> entries = new ArrayList<>();

        for (String line: lines) {
            LogEntry logEntry = LogEntryParser.parseLog(line);
            entries.add(logEntry);
        }

        return entries;
    }

    public static void printSummary(LogSummary summary) {
        System.out.println("Total lines: " + summary.getTotalCount());
        System.out.println("INFO: " + summary.getInfoCount());
        System.out.println("WARN: " + summary.getWarnCount());
        System.out.println("ERROR: " + summary.getErrorCount());
        System.out.println("UNKNOWN: " + summary.getUnknownCount());
    }

    public static void printLines(List<LogEntry> matches) {
        for (LogEntry match: matches) {
            System.out.println("\t" + match.getRawLine());
        }
        System.out.println("COUNT: " + matches.size());
    }

    public static List<LogEntry> loadEntries(String file) {
        List<String> lines = readAllLines(file);
        return parseEntries(lines);
    }

}
