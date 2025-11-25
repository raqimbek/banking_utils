package dev.andrylat.raqimbek.bankingutils.cli;

import dev.andrylat.raqimbek.bankingutils.cli.dialogs.CardValidatorDialog;
import dev.andrylat.raqimbek.bankingutils.cli.dialogs.MortgageCalculatorDialog;
import dev.andrylat.raqimbek.bankingutils.cli.dialogs.Dialog;
import dev.andrylat.raqimbek.bankingutils.cli.services.userinteraction.UserInteraction;
import dev.andrylat.raqimbek.bankingutils.cli.services.userinteraction.CommandLineUserInteraction;

import java.util.Scanner;
import java.util.Map;

public class BankingUtilsApp {
  private static final UserInteraction commandLineUserInteraction =
      new CommandLineUserInteraction(new Scanner(System.in), System.out);

  public static void main(String[] args) {
    var dialogMap = getDialogMap();

    selectDialog(dialogMap).run();
  }

  private static String getBankingServiceSelectionPromptMessage(Map<Integer, Dialog> dialogMap) {
    var greetingMessage =
        new StringBuilder("Hello. Please type the index of the service you need:\n");

    dialogMap.entrySet().stream()
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

  private static Dialog selectDialog(Map<Integer, Dialog> dialogMap) {

    var promptMessage = getBankingServiceSelectionPromptMessage(dialogMap);

    commandLineUserInteraction.write(promptMessage);

    var selectedBankingService = -1;

    do {
      var input = commandLineUserInteraction.read();

      if (!isValidBankingServiceIndex(input, dialogMap)) {
        commandLineUserInteraction.write(
            "Please write only a number representing an index of a service.");
      }

      selectedBankingService = Integer.parseInt(input);

    } while (selectedBankingService < 0);

    return dialogMap.get(selectedBankingService);
  }

  private static boolean isValidBankingServiceIndex(String input, Map<Integer, Dialog> dialogMap) {
    final var NON_NEGATIVE_INTEGER_PATTERN = "^(0|[1-9]\\d*)$";
    return input.matches(NON_NEGATIVE_INTEGER_PATTERN) && dialogMap.containsKey(Integer.parseInt(input));
  }

  private static Map<Integer, Dialog> getDialogMap() {
    return Map.of(
        0,
        new CardValidatorDialog(commandLineUserInteraction),
        1,
        new MortgageCalculatorDialog(commandLineUserInteraction));
  }
}
