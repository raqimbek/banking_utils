package dev.andrylat.raqimbek.bankingutils.cli.service.userinteraction;

import lombok.AllArgsConstructor;

import java.util.Scanner;
import java.util.List;
import java.io.PrintStream;

@AllArgsConstructor
public class CommandLineUserInteraction implements UserInteraction {
  private final PrintStream out;
  private final Scanner scanner;

  public void write(String message) {
    if (message != null) {
        out.println(message);
    }
  }

  public void writeAll(List<String> messages) {
    if (messages != null && !messages.isEmpty()) {
        messages.forEach(m -> write(new StringBuilder("-> ").append(m).toString()));
    }
  }

  public String read() {
    return scanner.nextLine();
  }

  public int readInt() {
      var input = scanner.nextInt();
      scanner.nextLine();

      return input;
  }
}
