package dev.andrylat.raqimbek.bankingutils.core.mortgageutility.validator;

import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageData;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MortgageDataValidator implements Validator<MortgageData> {
  private static final double MINIMUM_BORROWED_AMOUNT = 1.0;

  public MortgageDataValidationResult validate(MortgageData mortgageData) {
    if (mortgageData != null) {
      var errors = generateErrors(mortgageData);

      return new MortgageDataValidationResult(errors.isEmpty(), errors);
    } else {
      return new MortgageDataValidationResult(false, null);
    }
  }

  private List<String> generateErrors(MortgageData mortgageData) {
    var errors = new ArrayList<String>();

    if (!isPositiveDecimalNumbers(mortgageData.borrowedAmount().toString(), mortgageData.annualInterestRate().toString(), mortgageData.numberOfYearsToPay().toString())) {
      errors.add("Only positive decimal numbers are allowed.");
    }

    if (!greaterThanMinimumBorrowedAmount(mortgageData.borrowedAmount().toString())) {
      errors.add("Minimum borrowed amount must be greater than or equal to 1.");
    }

    return errors;
  }

  private static boolean isPositiveDecimalNumbers(String... numbers) {
    return Arrays.stream(numbers).allMatch(MortgageDataValidator::isPositiveDecimalNumber);
  }

  private static boolean isPositiveDecimalNumber(String number) {
    final var POSITIVE_DECIMAL_PATTERN = "^((\\d+\\.\\d+)|(\\.\\d+)|(\\d+))$";
    return number.matches(POSITIVE_DECIMAL_PATTERN);
  }

  private static boolean greaterThanMinimumBorrowedAmount(String borrowedAmount) {
    return Double.parseDouble(borrowedAmount) >= MINIMUM_BORROWED_AMOUNT;
  }
}
