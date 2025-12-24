package dev.andrylat.raqimbek.bankingutils.cli.dialog;

import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageCalculator;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageData;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.validator.MortgageDataValidator;
import dev.andrylat.raqimbek.bankingutils.cli.userinteraction.UserInteraction;
import lombok.AllArgsConstructor;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
public class MortgageCalculatorDialog implements Dialog {
    private final UserInteraction USER_INTERACTION;
    private final MortgageDataValidator VALIDATOR;
    private final MortgageCalculator CALCULATOR;


    public void run() {
        promptForMonthlyMortgagePaymentCalculatorData().ifPresent(mortgageData -> {
            var validationResult = VALIDATOR.validate(mortgageData);
            var message = "Input data is incorrect. Errors:";

            if (validationResult.isValid() && validationResult.errors() == null) {
                var monthlyPayment = CALCULATOR.calculateMonthlyMortgagePayment(mortgageData);
                message = "Your monthly mortgage payment is %s".formatted(monthlyPayment);

                USER_INTERACTION.write(message);
            }
            USER_INTERACTION.write(message);
            USER_INTERACTION.writeAll(validationResult.errors());

        });
    }

    private Optional<MortgageData> promptForMonthlyMortgagePaymentCalculatorData() {
        String promptMessage =
                "Please enter information regarding your mortgage in the following order and separate lines:\n-amount of money you borrowed \n- annual interest rate\n- number of years you have to pay\n";

        USER_INTERACTION.write(promptMessage);

        try {
            var borrowedAmount = USER_INTERACTION.readBigDecimal();
            var annualInterestRate = USER_INTERACTION.readBigDecimal();
            var numberOfYearsToPay = USER_INTERACTION.readBigDecimal();

            return Optional.of(new MortgageData(borrowedAmount, annualInterestRate, numberOfYearsToPay));
        } catch (InputMismatchException exception) {
            USER_INTERACTION.write("Mortgage data must be provided one value per line. Each line must contain only digits, with no spaces, hyphens, or other characters, and the line must not be empty.");
        } catch (NoSuchElementException exception) {
            USER_INTERACTION.write("The operation has been stopped by the user.");
        }

        return Optional.empty();

    }
}
