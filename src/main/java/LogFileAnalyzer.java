
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class LogFileAnalyzer {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("\tlogtool help");
            System.out.println("\tlogtool summary <file>");
            System.out.println("\tlogtool errors <file>");
            System.out.println("\tlogtool warnings <file>");
            System.out.println("\tlogtool search <keyword> <file>");
            return;
        }

        String command = args[0];

        switch (command) {
            case "help" -> {
                System.out.println("Usage:");
                System.out.println("\tlogtool help");
                System.out.println("\tlogtool summary <file>");
                System.out.println("\tlogtool errors <file>");
                System.out.println("\tlogtool warnings <file>");
                System.out.println("\tlogtool search <keyword> <file>");
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
                        try {
                            List<String> allLines = Files.readAllLines(Path.of(file));
                            System.out.println("Total lines: " + allLines.size());
                            return;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
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
                   String searchWord = args[1];
                   String file = args[2];

                   if (isValidFilePath(file)) {
                       System.out.println("Processing search command for: " + searchWord + " in " + file);
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
}
