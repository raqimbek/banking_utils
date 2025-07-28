package dev.andrylat.raqimbek.bankingutils.core.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MortgageInputValidator implements Validator {
  private static final double MINIMUM_BORROWED_AMOUNT = 1.0;

  public MortgageInputValidationInfo validate(List<String> inputList) {
    if (inputList != null && !inputList.isEmpty()) {
      var borrowedAmount = inputList.get(0);
      var annualInterestRate = inputList.get(1);
      var numberOfYears = inputList.get(2);
      var errors = generateErrors(borrowedAmount, annualInterestRate, numberOfYears);

      return new MortgageInputValidationInfo(errors.isEmpty(), errors);
    } else {
      return new MortgageInputValidationInfo(false, null);
    }
  }

  private List<String> generateErrors(String... input) {
    var errors = new ArrayList<String>();
    var borrowedAmount = input[0];
    var annualInterestRate = input[1];
    var numberOfYears = input[2];

    if (!isPositiveDecimalNumbers(borrowedAmount, annualInterestRate, numberOfYears)) {
      errors.add("Only positive decimal numbers are allowed.");
    }

    if (!greaterThanMinimumBorrowedAmount(borrowedAmount)) {
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
