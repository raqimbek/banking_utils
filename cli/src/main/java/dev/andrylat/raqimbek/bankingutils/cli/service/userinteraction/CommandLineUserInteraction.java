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
    out.println(message);
  }

  public void writeAll(List<String> messages) {
    messages.forEach(m -> write(new StringBuilder("-> ").append(m).toString()));
  }

  public String read() {
    return scanner.nextLine();
  }
}
