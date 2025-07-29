package dev.andrylat.raqimbek.bankingutils.cli.services.userinteraction;

import java.util.Scanner;
import java.util.List;
import java.io.PrintStream;

public class CommandLineUserInteraction implements UserInteraction {
  private final PrintStream out;
  private final Scanner scanner;

  public CommandLineUserInteraction(Scanner scanner, PrintStream out) {
    this.out = out;
    this.scanner = scanner;
  }

  public void write(String message) {
    out.println(message);
  }

  public void writeAll(List<String> messages) {
    messages.forEach(m -> write(new StringBuilder("-> ").append(m).toString()));
  }

  public String read() {
    return scanner.nextLine();
  }
}
