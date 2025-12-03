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
            var validationInfo = VALIDATOR.validate(mortgageData);

            if (validationInfo.isValid()) {
                var monthlyPayment = CALCULATOR.calculateMonthlyMortgagePayment(mortgageData);
                var message =
                        new StringBuilder("Your monthly mortgage payment is ").append(monthlyPayment).toString();
                USER_INTERACTION.write(message);
            } else if (validationInfo.errors() != null) {
                USER_INTERACTION.write("Input data is incorrect. Errors:");
                USER_INTERACTION.writeAll(validationInfo.errors());
            }
        });
    }

    private Optional<MortgageData> promptForMonthlyMortgagePaymentCalculatorData() {
        String promptMessage =
                "Please enter information regarding your mortgage in the following order and separate lines:\n-amount of money you borrowed \n- annual interest rate\n- number of years you have to pay\n";

        USER_INTERACTION.write(promptMessage);

        try {
            var borrowedAmount = USER_INTERACTION.readBigDecimal();
            var annualInterestRate = USER_INTERACTION.readBigDecimal();
            var numberOfYears = USER_INTERACTION.readBigDecimal();

            return Optional.of(new MortgageData(borrowedAmount, annualInterestRate, numberOfYears));
        } catch (InputMismatchException exception) {
            USER_INTERACTION.write("Mortgage data must be provided one value per line. Each line must contain only digits, with no spaces, hyphens, or other characters, and the line must not be empty.");
        } catch (NoSuchElementException exception) {
            USER_INTERACTION.write("The operation has been stopped by the user.");
        }

        return Optional.empty();

    }
}
