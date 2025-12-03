package dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service;

import java.math.BigDecimal;

public record MortgageData(BigDecimal borrowedAmount, BigDecimal annualInterestRate, BigDecimal numberOfYears) {
}
