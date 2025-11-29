package dev.andrylat.raqimbek.bankingutils.cli.dialog;

import dev.andrylat.raqimbek.bankingutils.cli.service.userinteraction.UserInteraction;
import dev.andrylat.raqimbek.bankingutils.core.validator.CardValidator;
import dev.andrylat.raqimbek.bankingutils.core.service.paymentsystemdeterminer.PaymentSystemDeterminer;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
public class CardValidatorDialog implements Dialog {
    private final UserInteraction userInteraction;
    private final CardValidator validator;
    private final PaymentSystemDeterminer paymentSystemDeterminer;

    public void run() {
        promptForCardNumber().ifPresent(cardNumber -> {
            var validationInfo = validator.validate(cardNumber);

            if (validationInfo.isValid()) {
                var paymentSystemOptional = paymentSystemDeterminer.determinePaymentSystem(cardNumber);
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
        });


    }

    private Optional<BigDecimal> promptForCardNumber() {
        String promptMessage = "Enter card number for validation:";
        userInteraction.write(promptMessage);
        try {
            return Optional.of(userInteraction.readBigDecimal());
        } catch (InputMismatchException exception) {
            userInteraction.write("The card number must contain only digits, with no spaces, hyphens, or other characters");
        } catch (NoSuchElementException exception) {
            userInteraction.write("The operation has been stopped by the user.");
        }
        return Optional.empty();
    }
}
