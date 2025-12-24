package dev.andrylat.raqimbek.bankingutils.cli.dialog;

import dev.andrylat.raqimbek.bankingutils.cli.userinteraction.UserInteraction;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.validator.CardValidator;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.service.paymentsystemdeterminer.PaymentSystemDeterminer;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
public class CardValidatorDialog implements Dialog {
    private final UserInteraction userInteraction;
    private final CardValidator validator;
    private final PaymentSystemDeterminer paymentSystemDeterminer;

    public void run() {
        promptForCardNumber().ifPresent(cardNumber -> {
            var validationResult = validator.validate(cardNumber);

            if (validationResult.isValid()) {
                var paymentSystemOptional = paymentSystemDeterminer.determinePaymentSystem(cardNumber);
                var message =
                        paymentSystemOptional
                                .map(
                                        "Card number is valid. Payment System: %s"::formatted)
                                .orElse("Something went wrong... Payment system could not be determined.");

                userInteraction.write(message);
            } else if (validationResult.errors() != null) {
                userInteraction.write("Card number is not valid. Errors:");
                userInteraction.writeAll(validationResult.errors());
            }
        });


    }

    private Optional<BigDecimal> promptForCardNumber() {
        String promptMessage = "Enter card number for validation:";
        userInteraction.write(promptMessage);
        try {
            return Optional.of(userInteraction.readBigDecimal());
        } catch (InputMismatchException exception) {
            userInteraction.write("The card number must not be an empty line. It must contain only digits and must not include spaces, hyphens, or other characters.");
        } catch (NoSuchElementException exception) {
            userInteraction.write("The operation has been stopped by the user.");
        }
        return Optional.empty();
    }
}
