package dev.andrylat.raqimbek.bankingutils.cli.dialog;

import dev.andrylat.raqimbek.bankingutils.core.service.mortgagecalculator.MortgageCalculator;
import dev.andrylat.raqimbek.bankingutils.core.service.mortgagecalculator.MortgageData;
import dev.andrylat.raqimbek.bankingutils.core.validator.MortgageInput;
import dev.andrylat.raqimbek.bankingutils.core.validator.MortgageInputValidator;
import dev.andrylat.raqimbek.bankingutils.cli.service.userinteraction.UserInteraction;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class MortgageCalculatorDialog implements Dialog {
  private final UserInteraction userInteraction;

  public void run() {
    var validator = new MortgageInputValidator();
    var calculator = new MortgageCalculator();
    var mortgageInput = promptForMonthlyMortgagePaymentCalculatorData();
    var validationInfo = validator.validate(mortgageInput);

    if (validationInfo.isValid()) {
      var monthlyPayment = calculator.calculateMonthlyMortgagePayment(new MortgageData(new BigDecimal(mortgageInput.borrowedAmount()), new BigDecimal(mortgageInput.annualInterestRate()), new BigDecimal(mortgageInput.numberOfYears())));
      var message =
          new StringBuilder("Your monthly mortgage payment is ").append(monthlyPayment).toString();
      userInteraction.write(message);
    } else if (validationInfo.errors() != null) {
      userInteraction.write("Input data is incorrect. Errors:");
      userInteraction.writeAll(validationInfo.errors());
    }
  }

  private MortgageInput promptForMonthlyMortgagePaymentCalculatorData() {
    String promptMessage =
        "Please enter information regarding your mortgage in the following order and separate lines:\n-amount of money you borrowed \n- annual interest rate\n- number of years you have to pay\n";

    userInteraction.write(promptMessage);

    var borrowedAmount = userInteraction.read();
    var annualInterestRate = userInteraction.read();
    var numberOfYears = userInteraction.read();

    return new MortgageInput(borrowedAmount, annualInterestRate, numberOfYears);
  }
}
