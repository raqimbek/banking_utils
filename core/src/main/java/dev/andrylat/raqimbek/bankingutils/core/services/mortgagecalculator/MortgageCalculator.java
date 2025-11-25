package dev.andrylat.raqimbek.bankingutils.core.services.mortgagecalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MortgageCalculator {
  public BigDecimal calculateMonthlyMortgagePayment(MortgageData mortgageData) {
    var borrowedAmount = mortgageData.borrowedAmount();
    var annualInterestRate = mortgageData.annualInterestRate();
    var numberOfYears = mortgageData.numberOfYears();

    annualInterestRate = annualInterestRate.divide(new BigDecimal(100), RoundingMode.HALF_EVEN);

    var payBackForEveryCurrencyUnit =
        BigDecimal.ONE
            .add(
                annualInterestRate
                    .multiply(numberOfYears)
                    .divide(new BigDecimal(2), RoundingMode.HALF_EVEN))
            .add(
                annualInterestRate
                    .multiply(numberOfYears)
                    .pow(2)
                    .divide(new BigDecimal(12), RoundingMode.HALF_EVEN));
    var numberOfMonthlyPayments = numberOfYears.multiply(new BigDecimal(12));
    return borrowedAmount
        .multiply(payBackForEveryCurrencyUnit)
        .divide(numberOfMonthlyPayments, RoundingMode.HALF_EVEN);
  }
}
