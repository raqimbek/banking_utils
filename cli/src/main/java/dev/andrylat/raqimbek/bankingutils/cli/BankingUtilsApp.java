package dev.andrylat.raqimbek.bankingutils.cli;

import dev.andrylat.raqimbek.bankingutils.cli.dialog.CardValidatorDialog;
import dev.andrylat.raqimbek.bankingutils.cli.dialog.MortgageCalculatorDialog;
import dev.andrylat.raqimbek.bankingutils.cli.dialog.Dialog;
import dev.andrylat.raqimbek.bankingutils.cli.userinteraction.UserInteraction;
import dev.andrylat.raqimbek.bankingutils.cli.userinteraction.CommandLineUserInteraction;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageCalculator;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.service.paymentsystemdeterminer.PaymentSystemDeterminer;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.validator.CardValidator;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.validator.MortgageDataValidator;

import java.util.Scanner;
import java.util.Map;

public class BankingUtilsApp {
  private static final UserInteraction commandLineUserInteraction =
      new CommandLineUserInteraction(System.out, new Scanner(System.in));
  private static final CardValidator cardValidator = new CardValidator();
  private static final PaymentSystemDeterminer paymentSystemDeterminer = new PaymentSystemDeterminer();
  private static final MortgageDataValidator mortgageDataValidator = new MortgageDataValidator();
  private static final MortgageCalculator mortgageCalculator = new MortgageCalculator();
  private static final Map<Integer, Dialog> DIALOG_MAP = Map.of(
        0,
        new CardValidatorDialog(commandLineUserInteraction, cardValidator, paymentSystemDeterminer),
        1,
        new MortgageCalculatorDialog(commandLineUserInteraction, mortgageDataValidator, mortgageCalculator));

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

    commandLineUserInteraction.write(promptMessage);

    var selectedBankingService = -1;

    do {
      var input = commandLineUserInteraction.read();

      if (!isValidBankingServiceIndex(input)) {
        commandLineUserInteraction.write(
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
