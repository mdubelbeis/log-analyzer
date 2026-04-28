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
                } else {
                    System.out.println("Processing summary command");
                }
            }
            case "errors" -> {
                if (args.length == 1) {
                    System.out.println("Missing file path.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool errors <file>");
                } else {
                    System.out.println("Processing errors command");
                }
            }
            case "warnings" -> {
                if (args.length == 1) {
                    System.out.println("Missing file path.");
                    System.out.println("Usage:");
                    System.out.println("\tlogtool warnings <file>");
                } else {
                    System.out.println("Processing warnings command");
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
                } else {
                   System.out.println("Processing search command");
               }
            }
            default -> {
                System.out.println("Unknown command: " + args[0]);
                System.out.println("Run 'logtool help' to see available commands");
            }
        }

    }
}
