package dev.andrylat.raqimbek.bankingutils.cli.dialogs;

import dev.andrylat.raqimbek.bankingutils.cli.services.userinteraction.UserInteraction;
import dev.andrylat.raqimbek.bankingutils.core.validators.CardValidator;
import dev.andrylat.raqimbek.bankingutils.core.services.paymentsystemdeterminer.PaymentSystemDeterminer;
import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor
public class CardValidatorDialog implements Dialog {
  private final UserInteraction userInteraction;

  public void run() {
    var paymentSystemDeterminer = new PaymentSystemDeterminer();
    var validator = new CardValidator();
    var inputList = promptForCardNumber();
    var validationInfo = validator.validate(List.of(inputList));

    if (validationInfo.isValid()) {
      var paymentSystemOptional = paymentSystemDeterminer.determinePaymentSystem(inputList);
      var message =
          paymentSystemOptional
              .map(
                  paymentSystem ->
                      new StringBuilder("Card number is valid. Payment System: ")
                          .append(paymentSystem)
                          .toString())
              .orElse("Something went wrong... Payment system could not be determined.");

      userInteraction.write(message);
    } else if (validationInfo.errors() != null) {
      userInteraction.write("Card number is not valid. Errors:");
      userInteraction.writeAll(validationInfo.errors());
    }
  }

  private String promptForCardNumber() {
    String promptMessage = "Enter card number for validation:";
    userInteraction.write(promptMessage);
    return userInteraction.read();
  }
}
