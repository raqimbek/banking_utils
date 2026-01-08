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
    private final UserInteraction userInteraction;
    private final MortgageDataValidator validator;
    private final MortgageCalculator calculator;


    public void run() {
        promptForMonthlyMortgagePaymentCalculatorData().ifPresent(mortgageData -> {
            var validationResult = validator.validate(mortgageData);
            var message = "Input data is incorrect. Errors:";

            if (validationResult.isValid() && validationResult.errors() == null) {
                var monthlyPayment = calculator.calculateMonthlyMortgagePayment(mortgageData);
                message = "Your monthly mortgage payment is %s".formatted(monthlyPayment);

                userInteraction.write(message);
            }
            userInteraction.write(message);
            userInteraction.writeAll(validationResult.errors());

        });
    }

    private Optional<MortgageData> promptForMonthlyMortgagePaymentCalculatorData() {
        String promptMessage =
                "Please enter information regarding your mortgage in the following order and separate lines:\n-amount of money you borrowed \n- annual interest rate\n- number of years you have to pay\n";

        userInteraction.write(promptMessage);

        try {
            var borrowedAmount = userInteraction.readBigDecimal();
            var annualInterestRate = userInteraction.readBigDecimal();
            var numberOfYearsToPay = userInteraction.readBigDecimal();

            return Optional.of(new MortgageData(borrowedAmount, annualInterestRate, numberOfYearsToPay));
        } catch (InputMismatchException exception) {
            userInteraction.write("Mortgage data must be provided one value per line. Each line must contain only digits, with no spaces, hyphens, or other characters, and the line must not be empty.");
        } catch (NoSuchElementException exception) {
            userInteraction.write("The operation has been stopped by the user.");
        }

        return Optional.empty();

    }
}
