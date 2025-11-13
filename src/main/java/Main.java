import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {
  private static List<String> builtinCommands = List.of("exit", "echo", "type");

  private static String[] parseCommand(String commandWithArguments) {
    return commandWithArguments.split("\s", 2);
  }

  private static boolean findInPath(String name, String[] paths, Predicate<File> filePredicate) {
    for (String path : paths) {
      File directory = new File(path);
      File[] listFiles = directory.listFiles();
      if (listFiles != null) {
        for (File file : listFiles) {
          if (filePredicate.test(file)) {
            System.out.println(name + " is " + file.getAbsolutePath());
            return true;
          }
        }
      }
    }
    return false;
  }

  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.print("$ ");

      String commandWithArguments = scanner.nextLine();
      String[] parts = parseCommand(commandWithArguments);
      String command = parts[0];
      String arguments = parts.length > 1 ? parts[1] : null;

      if ("exit".equals(command)) {
        System.exit(Integer.parseInt(arguments));
      } else if ("echo".equals(command)) {
        System.out.println(arguments);
      } else if ("type".equals(command)) {
        if (builtinCommands.contains(arguments)) {
          System.out.println(arguments + " is a shell builtin");
        } else {
          String[] paths = System.getenv("PATH").split(File.pathSeparator);
          boolean findFlag = findInPath(arguments, paths, file -> file.getName().equals(arguments) && file.canExecute());
          if (!findFlag) {
            System.out.println(arguments + ": not found");
          }
        }
      } else {
        System.out.println(commandWithArguments + ": command not found");
      }
    }
  }
}
