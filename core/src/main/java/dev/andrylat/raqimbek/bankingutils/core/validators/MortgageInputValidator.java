package dev.andrylat.raqimbek.bankingutils.core.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MortgageInputValidator implements Validator<MortgageInput> {
  private static final double MINIMUM_BORROWED_AMOUNT = 1.0;

  public MortgageInputValidationInfo validate(MortgageInput mortgageInput) {
    if (mortgageInput != null) {
      var errors = generateErrors(mortgageInput);

      return new MortgageInputValidationInfo(errors.isEmpty(), errors);
    } else {
      return new MortgageInputValidationInfo(false, null);
    }
  }

  private List<String> generateErrors(MortgageInput mortgageInput) {
    var errors = new ArrayList<String>();

    if (!isPositiveDecimalNumbers(mortgageInput.borrowedAmount(), mortgageInput.annualInterestRate(), mortgageInput.numberOfYears())) {
      errors.add("Only positive decimal numbers are allowed.");
    }

    if (!greaterThanMinimumBorrowedAmount(mortgageInput.borrowedAmount())) {
      errors.add("Minimum borrowed amount must be greater than or equal to 1.");
    }

    return errors;
  }

  private static boolean isPositiveDecimalNumbers(String... numbers) {
    return Arrays.stream(numbers).allMatch(MortgageInputValidator::isPositiveDecimalNumber);
  }

  private static boolean isPositiveDecimalNumber(String number) {
    final var POSITIVE_DECIMAL_PATTERN = "^((\\d+\\.\\d+)|(\\.\\d+)|(\\d+))$";
    return number.matches(POSITIVE_DECIMAL_PATTERN);
  }

  private static boolean greaterThanMinimumBorrowedAmount(String borrowedAmount) {
    return Double.parseDouble(borrowedAmount) >= MINIMUM_BORROWED_AMOUNT;
  }
}
