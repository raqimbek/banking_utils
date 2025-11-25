package dev.andrylat.raqimbek.bankingutils.cli;

import dev.andrylat.raqimbek.bankingutils.cli.dialog.CardValidatorDialog;
import dev.andrylat.raqimbek.bankingutils.cli.dialog.MortgageCalculatorDialog;
import dev.andrylat.raqimbek.bankingutils.cli.dialog.Dialog;
import dev.andrylat.raqimbek.bankingutils.cli.service.userinteraction.UserInteraction;
import dev.andrylat.raqimbek.bankingutils.cli.service.userinteraction.CommandLineUserInteraction;
import java.util.Scanner;
import java.util.Map;

public class BankingUtilsApp {
  private static final UserInteraction COMMAND_LINE_USER_INTERACTION =
      new CommandLineUserInteraction(new Scanner(System.in), System.out);
  private static final Map<Integer, Dialog> DIALOG_MAP = Map.of(
        0,
        new CardValidatorDialog(COMMAND_LINE_USER_INTERACTION),
        1,
        new MortgageCalculatorDialog(COMMAND_LINE_USER_INTERACTION));

  public static void main(String[] args) {
    selectDialog().run();
  }

  private static String getBankingServiceSelectionPromptMessage() {
    var greetingMessage =
        new StringBuilder("Hello. Please type the index of the service you need:\n");

    DIALOG_MAP.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(
            e ->
                greetingMessage
                    .append("[")
                    .append(e.getKey())
                    .append("] - ")
                    .append(e.getValue())
                    .append("\n"));

    return greetingMessage.toString();
  }

  private static Dialog selectDialog() {

    var promptMessage = getBankingServiceSelectionPromptMessage();

    COMMAND_LINE_USER_INTERACTION.write(promptMessage);

    var selectedBankingService = -1;

    do {
      var input = COMMAND_LINE_USER_INTERACTION.read();

      if (!isValidBankingServiceIndex(input)) {
        COMMAND_LINE_USER_INTERACTION.write(
            "Please write only a number representing an index of a service.");
      }

      selectedBankingService = Integer.parseInt(input);

    } while (selectedBankingService < 0);

    return DIALOG_MAP.get(selectedBankingService);
  }

  private static boolean isValidBankingServiceIndex(String input) {
    final var NON_NEGATIVE_INTEGER_PATTERN = "^(0|[1-9]\\d*)$";
    return input.matches(NON_NEGATIVE_INTEGER_PATTERN) && DIALOG_MAP.containsKey(Integer.parseInt(input));
  }
}
