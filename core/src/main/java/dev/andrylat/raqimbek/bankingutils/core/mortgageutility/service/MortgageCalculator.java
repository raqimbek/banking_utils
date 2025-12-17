package dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class MortgageCalculator {
    /**
     * Calculates the monthly mortgage payment using a simple series approximation.
     *
     * <p><strong>Full formula:</strong>
     * <br>
     * M̂ = (B * P̂) / N = (B / (12 * T)) * (1 + (R * T) / 2 + (R * T)^2 / 12)
     *
     * <p><strong>Where:</strong>
     * <ul>
     *   <li>M̂ — monthly payment</li>
     *   <li>B — principal (total borrowed)</li>
     *   <li>T — loan term in years</li>
     *   <li>R — annual interest rate</li>
     *   <li>P̂ — amount repaid per currency unit borrowed
     *       (1 + linear interest approximation + quadratic correction)</li>
     *   <li>N = 12 * T — total number of monthly payments</li>
     * </ul>
     *
     * <p>The formula approximates interest assuming the balance declines linearly,
     * with a small second-order correction for improved accuracy.</p>
     *
     * <p><strong>Using constants:</strong>
     * <br>
     * LINEAR_INTEREST_APPROXIMATION = (R * T) / 2
     * <br>
     * QUADRATIC_CORRECTION = (R * T)^2 / 12
     * <br>
     * M̂ = (B / (12 * T)) * (1 + LINEAR_INTEREST_APPROXIMATION + QUADRATIC_CORRECTION)
     *
     * <p><strong>Explanation:</strong>
     * <ul>
     *   <li>
     *   <strong>LINEAR_INTEREST_APPROXIMATION</strong>: estimates most of the interest assuming the balance
     *   declines evenly from full principal (B) to zero,
     *   so the average balance (the arithmetic median) is (B + 0) / 2 = B / 2.
     *   It grows proportionally with rate and time (linear estimate).
     *   </li>
     *   <li>
     *   <strong>QUADRATIC_CORRECTION</strong>: small adjustment to improve accuracy, accounting for
     *   the nonlinear decline of the balance. It grows with (R * T)^2 and is usually small.
     *   </li>
     * @param mortgageData a record containing:
     * <ul>
     *   <li>borrowedAmount — total principal borrowed (B)</li>
     *   <li>annualInterestRate — annual interest rate as a percentage (R)</li>
     *   <li>numberOfYears — loan term in years (T)</li>
     * </ul>
     * @return the estimated monthly mortgage payment (M̂)
     */
    public BigDecimal calculateMonthlyMortgagePayment(MortgageData mortgageData) {
        final BigDecimal BORROWED_AMOUNT = mortgageData.borrowedAmount();
        final BigDecimal ANNUAL_INTEREST_RATE = convertPercentToDecimal(mortgageData.annualInterestRate());
        final BigDecimal NUMBER_OF_YEARS_TO_PAY = mortgageData.numberOfYearsToPay();
        final BigDecimal MONTHS_PER_YEAR = new BigDecimal("12");

        final BigDecimal NUMBER_OF_MONTHLY_PAYMENTS = NUMBER_OF_YEARS_TO_PAY.multiply(MONTHS_PER_YEAR);
        final BigDecimal PAYBACK_FOR_EVERY_CURRENCY_UNIT = getPaybackForEveryCurrencyUnit(ANNUAL_INTEREST_RATE, NUMBER_OF_YEARS_TO_PAY, MONTHS_PER_YEAR);

        return BORROWED_AMOUNT
                .multiply(PAYBACK_FOR_EVERY_CURRENCY_UNIT)
                .divide(NUMBER_OF_MONTHLY_PAYMENTS, 4, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getPaybackForEveryCurrencyUnit(BigDecimal ANNUAL_INTEREST_RATE, BigDecimal YEARS_TO_PAY, BigDecimal MONTHS_PER_YEAR) {
        final BigDecimal LINEAR_INTEREST_APPROXIMATION = ANNUAL_INTEREST_RATE
                .multiply(getAverageOf(YEARS_TO_PAY, BigDecimal.ZERO));

        final BigDecimal QUADRATIC_CORRECTION = ANNUAL_INTEREST_RATE
                .multiply(YEARS_TO_PAY)
                .pow(2)
                .divide(MONTHS_PER_YEAR, 4, RoundingMode.HALF_EVEN);

        return BigDecimal.ONE
                        .add(LINEAR_INTEREST_APPROXIMATION)
                        .add(QUADRATIC_CORRECTION);
    }

    private BigDecimal convertPercentToDecimal(BigDecimal percent) {
        if (percent.compareTo(BigDecimal.TEN) > 0) {
            return percent.divide(new BigDecimal(100), 4, RoundingMode.HALF_EVEN);
        }
        return percent;
    }

    private BigDecimal getAverageOf(BigDecimal... numbers) {
        return Arrays.stream(numbers).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(numbers.length), 4, RoundingMode.HALF_EVEN);
    }
}
