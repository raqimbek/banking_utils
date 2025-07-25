package dev.andrylat.raqimbek.bankingutils.core.services.mortgagecalculator;

public record MonthlyMortgagePaymentTestData(
    double borrowedAmount,
    double annualInterestRate,
    double numberOfYears,
    long expectedMonthlyPayment) {}
