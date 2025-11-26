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
  private final UserInteraction USER_INTERACTION;
  private final MortgageInputValidator VALIDATOR;
  private final MortgageCalculator CALCULATOR;


  public void run() {
    var mortgageInput = promptForMonthlyMortgagePaymentCalculatorData();
    var validationInfo = VALIDATOR.validate(mortgageInput);

    if (validationInfo.isValid()) {
      var monthlyPayment = CALCULATOR.calculateMonthlyMortgagePayment(new MortgageData(new BigDecimal(mortgageInput.borrowedAmount()), new BigDecimal(mortgageInput.annualInterestRate()), new BigDecimal(mortgageInput.numberOfYears())));
      var message =
          new StringBuilder("Your monthly mortgage payment is ").append(monthlyPayment).toString();
      USER_INTERACTION.write(message);
    } else if (validationInfo.errors() != null) {
      USER_INTERACTION.write("Input data is incorrect. Errors:");
      USER_INTERACTION.writeAll(validationInfo.errors());
    }
  }

  private MortgageInput promptForMonthlyMortgagePaymentCalculatorData() {
    String promptMessage =
        "Please enter information regarding your mortgage in the following order and separate lines:\n-amount of money you borrowed \n- annual interest rate\n- number of years you have to pay\n";

    USER_INTERACTION.write(promptMessage);

    var borrowedAmount = USER_INTERACTION.read();
    var annualInterestRate = USER_INTERACTION.read();
    var numberOfYears = USER_INTERACTION.read();

    return new MortgageInput(borrowedAmount, annualInterestRate, numberOfYears);
  }
}
