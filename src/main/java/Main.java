import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  private static List<String> builtinCommands = List.of("exit", "echo", "type");
  private static String command;
  private static String arguments;

  private static void parse(String commandWithArguments) {
    String[] splits = commandWithArguments.split("\s", 2);
    command = splits[0];
    arguments = null;
    if (splits.length > 1) {
      arguments = splits[1];
    }
  }

  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.print("$ ");

      String commandWithArguments = scanner.nextLine();
      parse(commandWithArguments);

      if ("exit".equals(command)) {
        System.exit(Integer.parseInt(arguments));
      } else if ("echo".equals(command)) {
        System.out.println(arguments);
      } else if ("type".equals(command)) {
        if (builtinCommands.contains(arguments)) {
          System.out.println(arguments + " is a shell builtin");
        } else {
          String[] paths = System.getenv("PATH").split(File.pathSeparator);
          boolean findFlag = false;

          outer:
          for (String path : paths) {
            File directory = new File(path);
            File[] listFiles = directory.listFiles();
            if (listFiles != null) {
              for (File file : listFiles) {
                if (file.getName().equals(arguments) && file.canExecute()) {
                  System.out.println(arguments + " is " + file.getAbsolutePath());
                  findFlag = true;
                  break outer;
                }
              }
            }
          }

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
