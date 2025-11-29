package dev.andrylat.raqimbek.bankingutils.cli;

import dev.andrylat.raqimbek.bankingutils.cli.dialog.CardValidatorDialog;
import dev.andrylat.raqimbek.bankingutils.cli.dialog.MortgageCalculatorDialog;
import dev.andrylat.raqimbek.bankingutils.cli.dialog.Dialog;
import dev.andrylat.raqimbek.bankingutils.cli.service.userinteraction.UserInteraction;
import dev.andrylat.raqimbek.bankingutils.cli.service.userinteraction.CommandLineUserInteraction;
import dev.andrylat.raqimbek.bankingutils.core.service.mortgagecalculator.MortgageCalculator;
import dev.andrylat.raqimbek.bankingutils.core.service.paymentsystemdeterminer.PaymentSystemDeterminer;
import dev.andrylat.raqimbek.bankingutils.core.validator.CardValidator;
import dev.andrylat.raqimbek.bankingutils.core.validator.MortgageDataValidator;

import java.util.Scanner;
import java.util.Map;

public class BankingUtilsApp {
  private static final UserInteraction COMMAND_LINE_USER_INTERACTION =
      new CommandLineUserInteraction(System.out, new Scanner(System.in));
  private static final CardValidator CARD_VALIDATOR = new CardValidator();
  private static final PaymentSystemDeterminer PAYMENT_SYSTEM_DETERMINER = new PaymentSystemDeterminer();
  private static final MortgageDataValidator MORTGAGE_INPUT_VALIDATOR = new MortgageDataValidator();
  private static final MortgageCalculator  MORTGAGE_CALCULATOR = new MortgageCalculator();
  private static final Map<Integer, Dialog> DIALOG_MAP = Map.of(
        0,
        new CardValidatorDialog(COMMAND_LINE_USER_INTERACTION,CARD_VALIDATOR, PAYMENT_SYSTEM_DETERMINER),
        1,
        new MortgageCalculatorDialog(COMMAND_LINE_USER_INTERACTION, MORTGAGE_INPUT_VALIDATOR, MORTGAGE_CALCULATOR));

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
