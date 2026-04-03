package dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MortgageCalculatorTest {
    private final MortgageCalculator mortgageCalculator = new MortgageCalculator();

    @ParameterizedTest
    @MethodSource("getMonthlyMortgagePaymentTestData")
    public void shouldReturnProperMonthlyMortgagePayment(MonthlyMortgagePaymentTestData testCaseData) {

        if (hasPositiveFinancialInputs(testCaseData)) {
            var mortgageData = new MortgageData(testCaseData.borrowedAmount(), testCaseData.annualInterestRate(), testCaseData.numberOfYears());
            var actual =
                    mortgageCalculator.calculateMonthlyMortgagePayment(mortgageData);
            assertEquals(testCaseData.expectedMonthlyPayment(), actual);
        }
    }

    private static List<MonthlyMortgagePaymentTestData> getMonthlyMortgagePaymentTestData() {
        var monthlyMortgagePaymentTestData = new ArrayList<MonthlyMortgagePaymentTestData>();

        monthlyMortgagePaymentTestData.add(
                new MonthlyMortgagePaymentTestData(
                        new BigDecimal("360000.0"),
                        new BigDecimal("7.5"),
                        new BigDecimal("30"),
                        new BigDecimal("3250.000")));
        monthlyMortgagePaymentTestData.add(
                new MonthlyMortgagePaymentTestData(
                        new BigDecimal("360000.0"),
                        new BigDecimal("5"),
                        new BigDecimal("30"),
                        new BigDecimal("1000.0")));
        monthlyMortgagePaymentTestData.add(
                new MonthlyMortgagePaymentTestData(
                        new BigDecimal("176000.0"),
                        new BigDecimal("4"),
                        new BigDecimal("30"),
                        new BigDecimal("488.9")));
        monthlyMortgagePaymentTestData.add(
                new MonthlyMortgagePaymentTestData(
                        new BigDecimal("1000000"),
                        new BigDecimal("12"),
                        new BigDecimal("10"),
                        new BigDecimal("8333")));

        return monthlyMortgagePaymentTestData;
    }

    private boolean hasPositiveFinancialInputs(MonthlyMortgagePaymentTestData testCaseData) {
        return testCaseData.annualInterestRate().compareTo(BigDecimal.ZERO) > 0
                && testCaseData.borrowedAmount().compareTo(BigDecimal.ZERO) > 0
                && testCaseData.numberOfYears().compareTo(BigDecimal.ZERO) > 0;
    }
}
