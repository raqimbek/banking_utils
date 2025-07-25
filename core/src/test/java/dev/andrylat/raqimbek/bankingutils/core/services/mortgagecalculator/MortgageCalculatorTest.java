package dev.andrylat.raqimbek.bankingutils.core.services.mortgagecalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MortgageCalculatorTest {
  private final MortgageCalculator mortgageCalculator = new MortgageCalculator();

  //  @Test
  //  public void shouldReturnProperMonthlyMortgagePayment() {
  //    var testCasesData = getMonthlyMortgagePaymentTestData();
  //
  //    testCasesData.forEach(
  //        (testCaseData) -> {
  //          if (testCaseData.annualInterestRate() > 0
  //              && testCaseData.borrowedAmount() > 0
  //              && testCaseData.numberOfYears() > 0) {
  //            var actual =
  //                mortgageCalculator.calculateMonthlyMortgagePayment(
  //                    List.of(
  //                        String.valueOf(testCaseData.borrowedAmount()),
  //                        String.valueOf(testCaseData.annualInterestRate()),
  //                        String.valueOf(testCaseData.numberOfYears())));
  //            assertEquals(testCaseData.expectedMonthlyPayment(), actual);
  //          }
  //        });
  //  }

  private List<MonthlyMortgagePaymentTestData> getMonthlyMortgagePaymentTestData() {
    var monthlyMortgagePaymentTestData = new ArrayList<MonthlyMortgagePaymentTestData>();

    monthlyMortgagePaymentTestData.add(
        new MonthlyMortgagePaymentTestData(360_000.0, 7.5, 30, 2_547));
    monthlyMortgagePaymentTestData.add(new MonthlyMortgagePaymentTestData(360_000.0, 5, 30, 1_938));
    monthlyMortgagePaymentTestData.add(new MonthlyMortgagePaymentTestData(176_000.0, 4, 30, 841));
    monthlyMortgagePaymentTestData.add(
        new MonthlyMortgagePaymentTestData(1_000_000, 12, 10, 14_333));

    return monthlyMortgagePaymentTestData;
  }
}
