import java.util.Scanner;

public class Main {
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
      } else {
        System.out.println(command + ": command not found");
      }
    }
  }
}
