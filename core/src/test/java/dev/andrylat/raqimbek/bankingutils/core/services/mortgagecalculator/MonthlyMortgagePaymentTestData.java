package dev.andrylat.raqimbek.bankingutils.core.services.mortgagecalculator;

import java.math.BigDecimal;

public record MonthlyMortgagePaymentTestData(
    BigDecimal borrowedAmount,
    BigDecimal annualInterestRate,
    BigDecimal numberOfYears,
    BigDecimal expectedMonthlyPayment) {}
