package dev.andrylat.raqimbek.bankingutils.core.service.mortgagecalculator;

import java.math.BigDecimal;

public record MortgageData(BigDecimal borrowedAmount, BigDecimal annualInterestRate, BigDecimal numberOfYears) {
}
