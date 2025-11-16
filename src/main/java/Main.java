import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {
    private static List<String> builtinCommands = List.of(
        "cd",
        "echo",
        "exit",
        "pwd",
        "type"
    );
    private static Path currentDirectory = Paths.get("");

    private static String[] parseCommand(String commandWithArguments) {
        return commandWithArguments.split("\s", 2);
    }

    private static boolean findFile(String[] paths, String name, Predicate<File> filePredicate, Consumer<File> fileConsumer) {
        for (String path : paths) {
            File directory = new File(path);
            File[] listFiles = directory.listFiles();
            if (listFiles != null) {
                for (File file : listFiles) {
                    if (file.getName().equals(name) && filePredicate.test(file)) {
                        if (fileConsumer != null) {
                            fileConsumer.accept(file);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void executeProcess(String[] paths, String command, String arguments) {
        boolean isFound = findFile(paths, command, f -> f.canExecute(), null);
        if (isFound) {
            List<String> commands = new ArrayList<>();
            commands.add(command);
            if (arguments != null) {
                for (String argument : arguments.split("\s")) {
                    commands.add(argument);
                }
            }

            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true);
            try {
                Process process = pb.start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(command + ": not found");
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");

            String commandWithArguments = scanner.nextLine();
            String[] parts = parseCommand(commandWithArguments);
            String command = parts[0];
            String arguments = parts.length > 1 ? parts[1] : null;

            String[] paths = System.getenv("PATH").split(File.pathSeparator);

            if (builtinCommands.contains(command)) {
                switch(command) {
                    case "cd" -> {
                        Path inputPath = Paths.get(arguments);
                        if (Files.isDirectory(inputPath)) {
                            currentDirectory = inputPath;
                        } else {
                            System.out.println(arguments + ": No such file or directory");
                        }
                    }
                    case "echo" -> System.out.println(arguments);
                    case "exit" -> System.exit(Integer.parseInt(arguments));
                    case "pwd" -> System.out.println(currentDirectory.toAbsolutePath().toString());
                    case "type" -> {
                        if (builtinCommands.contains(arguments)) {
                            System.out.println(arguments + " is a shell builtin");
                        } else {
                            boolean isFound = findFile(paths, arguments, f -> f.canExecute(), f -> System.out.println(arguments + " is " + f.getAbsolutePath()));
                            if (!isFound) {
                                System.out.println(arguments + ": not found");
                            }
                        }
                    }
                }
            } else {
                executeProcess(paths, command, arguments);
            }
        }
    }

}
