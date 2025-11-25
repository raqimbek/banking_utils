package dev.andrylat.raqimbek.bankingutils.core.services.mortgagecalculator;

import java.math.BigDecimal;

public record MortgageData(BigDecimal borrowedAmount, BigDecimal annualInterestRate, BigDecimal numberOfYears) {
}
