
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class LogFileAnalyzer {
    public static void main(String[] args) {
        if (args.length == 0) {
            displayHelp();
            return;
        }

        String command = args[0];

        switch (command) {
            case "help" -> {
                displayHelp();
            }
            case "summary" -> {
                if (args.length == 1) {
                    System.out.println("Missing file path.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool summary <file>");
                } else if (args.length == 2) {
                    String file = args[1];

                    if (isValidFilePath(file)) {
                        System.out.println("Processing summary command for: " + file);
                        printSummary(file);
                    }

                } else {
                    System.out.println("Too many arguments passed.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool summary <file>");
                }
            }
            case "errors" -> {
                if (args.length == 1) {
                    System.out.println("Missing file path.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool errors <file>");
                } else if (args.length == 2) {
                    String file = args[1];

                    if (isValidFilePath(file)) {
                        System.out.println("Processing errors command for: " + file);
                        printLinesByLevel(file, "ERROR");
                    }
                } else {
                    System.out.println("Too many arguments passed.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool errors <file>");
                }
            }
            case "warnings" -> {
                if (args.length == 1) {
                    System.out.println("Missing file path.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool warnings <file>");
                } else if (args.length == 2) {
                    String file = args[1];

                    if (isValidFilePath(file)) {
                        System.out.println("Processing warnings command for: " + file);
                        printLinesByLevel(file, "WARN");
                    }
                } else {
                    System.out.println("Too many arguments passed.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool warnings <file>");
                }
            }
            case "search" -> {
               if (args.length == 1) {
                    System.out.println("Missing keyword and file.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool search <keyword> <file>");
                } else if (args.length == 2) {
                    System.out.println("Missing file.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool search <keyword> <file>");
                } else if (args.length == 3) {
                   String originalKeyword = args[1];
                   String keyword = originalKeyword.toLowerCase();
                   String file = args[2];

                   if (isValidFilePath(file)) {
                       System.out.println("Processing search command for: " + originalKeyword + " in " + file);
                       searchLines(file, keyword);
                   }
               } else {
                   System.out.println("Too many arguments passed.");
                   System.out.println("Usage:");
                   System.out.println("\tlogtool search <keyword> <file>");
               }
            }
            default -> {
                System.out.println("Unknown command: " + args[0]);
                System.out.println("Run 'logtool help' to see available commands");
            }
        }

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
            return List.of(); // TODO: add more clear logic
        }
    }

    public static void printSummary(String file) {
        List<String> allLines = readAllLines(file);

        int infoCount = 0, warnCount = 0, errorCount = 0;
        for (String line: allLines) {
            if (line.contains("INFO")) infoCount += 1;
            if (line.contains("WARN")) warnCount += 1;
            if (line.contains("ERROR")) errorCount += 1;
        }

        System.out.println("Total lines: " + allLines.size());
        System.out.println("INFO: " + infoCount);
        System.out.println("WARN: " + warnCount);
        System.out.println("ERROR: " + errorCount);
    }

    public static void printLinesByLevel(String file, String level) {
        List<String> allLines = readAllLines(file);
        int matchCount = 0;
        for (String line: allLines) {
            if (line.contains(level)) {
                matchCount += 1;
                System.out.println("\t" + line);
            }
        }
        System.out.println("Total " + level + ": " + matchCount);
    }

    public static void searchLines(String file, String keyword) {
        List<String> allLines = readAllLines(file);
        int matchCount = 0;

        for (String line: allLines) {
            String lowerCaseLine = line.toLowerCase();
            if (lowerCaseLine.contains(keyword)) {
                matchCount += 1;
                System.out.println("\t" + line);
            }
        }
        System.out.println("Total found: " + matchCount);
    }

}
