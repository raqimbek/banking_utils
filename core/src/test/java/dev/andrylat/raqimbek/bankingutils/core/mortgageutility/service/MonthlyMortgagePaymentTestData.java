package dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service;

import java.math.BigDecimal;

public record MonthlyMortgagePaymentTestData(
    BigDecimal borrowedAmount,
    BigDecimal annualInterestRate,
    BigDecimal numberOfYears,
    BigDecimal expectedMonthlyPayment) {}
