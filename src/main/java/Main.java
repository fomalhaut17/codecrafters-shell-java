import java.util.List;
import java.util.Scanner;

public class Main {
  private static List<String> builtinCommands = List.of("exit", "echo", "type");

  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.print("$ ");
      String command = scanner.next();

      if ("exit".equals(command)) {
        int code = scanner.nextInt();
        System.exit(code);
      } else if ("echo".equals(command)) {
        String parameters = scanner.nextLine();
        if (parameters != null) {
          parameters = parameters.trim();
        }
        System.out.println(parameters);
      } else if ("type".equals(command)) {
        String parameter = scanner.next();
        if (builtinCommands.contains(parameter)) {
          System.out.println(parameter + " is a shell builtin");
        } else {
          System.out.println(parameter + ": not found");
        }
      } else {
        System.out.println(command + ": command not found");
      }
    }
  }
}
