import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.print("$ ");
      String input = scanner.next();
      if ("exit".equals(input)) {
        int code = scanner.nextInt();
        System.exit(code);
      } else if ("echo".equals(input)) {
        String parameters = scanner.nextLine();
        if (parameters != null) {
          parameters = parameters.trim();
        }
        System.out.println(parameters);
      } else {
        System.out.println(input + ": command not found");
      }
    }
  }
}
