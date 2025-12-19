package dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * <p>
 * MortgageCalculator provides a method to compute the monthly mortgage payment using a <strong>simplified</strong> series approximation.
 * The calculation is based on the Persian folk method <a href="https://nickarnosti.com/blog/mortgagepayments/">described by Nick Arnosti</a> and uses a linear approximation
 * for the interest with a small quadratic correction to improve accuracy.
 * </p>
 * <br>
 * <p>
 * <b>Primary method:</b> {@link #calculateMonthlyMortgagePayment(MortgageData)} takes a <code>MortgageData</code> record
 * as input and returns the estimated monthly mortgage payment.
 * </p>
 * <br>
 * <p>
 * <b>Monthly payment formula:</b>
 * <code>
 * M̂ = B * P̂ / N = (B / N) * (1 + (R * T) / 2 + (R * T)^2 / 12)
 * </code>
 * where:
 * <ul>
 *     <li><code>M̂</code> - monthly payment</li>
 *     <li><code>B</code> - total principal borrowed (<code>BORROWED_AMOUNT</code>)</li>
 *     <li><code>P̂</code> - total repayment per single currency unit (<code>TOTAL_REPAYMENT_PER_SINGLE_CURRENCY_UNIT</code>)</li>
 *     <li><code>N</code> - total number of monthly payments (<code>NUMBER_OF_MONTHLY_PAYMENTS = NUMBER_OF_YEARS_TO_PAY * 12</code>)</li>
 *     <li><code>1</code> - single currency unit taken as basis for scaling</li>
 *     <li><code>R</code> - annual interest rate as decimal (<code>ANNUAL_INTEREST_RATE</code>)</li>
 *     <li><code>T</code> - loan term in years (<code>NUMBER_OF_YEARS_TO_PAY</code>)</li>
 * </ul>
 * </p>
 * <br>
 * <br>
 * <p>
 * <b>Total repayment per single currency unit:</b>
 * <p>Simply put, <code>TOTAL_REPAYMENT_PER_SINGLE_CURRENCY_UNIT</code> represents how much a borrower
 * pays back <strong>in total</strong> for each $1 borrowed — <strong>including both</strong> the original $1 and the
 * interest accumulated over the loan term.</p>
 * <code>
 * Total repayment per single currency unit = SINGLE_CURRENCY_UNIT + LINEAR_INTEREST_APPROXIMATION_PER_SINGLE_CURRENCY_UNIT + QUADRATIC_CORRECTION_PER_SINGLE_CURRENCY_UNIT
 * </code>
 * <ul>
 *     <li><code>SINGLE_CURRENCY_UNIT = 1</code> represents one unit of principal.
 *         It is the basic unit used to calculate repayment before scaling up to the total principal.
 *         Simply put, it is <strong>$1 of the total borrowed amount</strong>.</li>
 *     <li>
 *         <p>
 *             <code>LINEAR_INTEREST_APPROXIMATION_PER_SINGLE_CURRENCY_UNIT = (R * T) / 2</code>
 *         </p>
 *         <p>
 *             The linear interest approximation estimates total interest as:
 *             <code>((B + 0) / 2) * R * T</code> which derives from the formula
 *  *             <code>Î = B * (P̂ - 1) =  B * (1 + (R * T) / 2 + (R * T)^2 / 12 - 1) = B * ((R * T) / 2 + (R * T)^2 / 12)</code>,
 *             where <code>B</code> is the borrowed amount, <code>R</code> is the annual interest rate,
 *             and <code>T</code> is the loan term in years.
 *         </p>
 *         <p>
 *             This approximation assumes the loan balance declines evenly from the full
 *             principal <code>B</code> to zero over the loan duration. Under this simplifying
 *             assumption, the average balance over time is the arithmetic mean
 *             <code>(B + 0) / 2</code>.
 *         </p>
 *         <p>
 *              Instead of summing the balance month by month (or unit by unit),
 *              the approximation assumes that the balance declines
 *              linearly from the full principal  <code>B</code> to zero.
 *         </p>
 *         <p>
 *             It is called “linear” because the estimate grows proportionally
 *             with both rate and time and captures
 *             most of the interest without modeling month-by-month balance changes.
 *         </p>
 *         <p>
 *             Here, the “set to calculate average of” is just the two endpoints - start and finish - not every individual currency unit.
 *             the "set" here is conceptual set of points on the balance-over-time line, not literal discrete currency units.<br>
 *             It’s a conceptual simplification. So, the “2 edges” (two endpoints of the linear decline) are used
 *             because it’s a linearly declining function,
 *             and for a straight line, the mean of just the endpoints equals
 *             <strong>the mean of the entire continuous function</strong>.<br>
 *             It's a shortcut that works because of the <strong>linearity assumption</strong>.
 *             This works because for a straight line,
 *             the arithmetic mean of the endpoints equals the integral (or sum) of
 *             all intermediate balances divided by the duration.
 *         </p>
 *         <p>
 *             To get the linear interest approximation <strong>per unit of principal</strong>
 *             (e.g., the linear extra amount on top of $1 of the full borrowed amount),
 *             we divide the total linear interest estimate
 *             by the total principal <code>B</code> (the full borrowed amount, e.g. $100_000):
 *             <br>
 *             <code>LINEAR_INTEREST_APPROXIMATION_PER_SINGLE_CURRENCY_UNIT = (((B + 0) / 2) * R * T)/B = ((B / 2) * R * T) / B = (1/2 * B * R * T) / B = B * (1/2 * R * T) / B = 1/2 * R * T = (R * T) / 2</code>
 *         </p>
 *     </li>
 *     <li>
 *         <code>QUADRATIC_CORRECTION_PER_SINGLE_CURRENCY_UNIT = (R * T)^2 / 12</code>
 *         <p>
 *             Quadratic correction estimates total interest as:
 *             <code>B * ((R * T)^2 / 12)</code> which derives from the formula
 *  *  *             <code>Î = B * (P̂ - 1) =  B * (1 + (R * T) / 2 + (R * T)^2 / 12 - 1) = B * ((R * T) / 2 + (R * T)^2 / 12)</code>
 *         </p>
 *         <p>Quadratic correction is a small adjustment to improve accuracy,
 *         accounting for the nonlinear decline of the balance.
 *         Its contribution is much smaller than the <em>linear interest approximation</em> for typical loans.
 *         The coefficient 12 comes from the Maclaurin series expansion (Taylor series at 0), <strong>not months</strong>.
 *         </p>
 *         <p>
 *             To get the quadratic correction <strong>per unit of principal</strong>
 *             (e.g., the linear extra amount on top of $1 of the full borrowed amount),
 *             we divide the total quadratic correction by the total principal <code>B</code>
 *             (the full borrowed amount, e.g. $100_000):
 *             <br>
 *             <code>QUADRATIC_CORRECTION_PER_SINGLE_CURRENCY_UNIT = (B * ((R * T)^2 / 12)) / B = B * ((R * T)^2 / 12) / B = (B / B) * ((R * T)^2 / 12) = (R * T)^2 / 12</code>
 *         </p>
 *     </li>
 * </ul>
 * </p>
 * <br>
 * <br>
 * <p>
 * <b>Alternate (plain English) formula representation:</b>
 * <br>
 * <code>
 * totalAmountToPayBack = borrowedAmount * totalRepaymentPerSingleCurrencyUnit
 * </code>
 * <br>
 * <code>
 * totalRepaymentPerSingleCurrencyUnit = SINGLE_CURRENCY_UNIT + LINEAR_INTEREST_APPROXIMATION_PER_SINGLE_CURRENCY_UNIT + QUADRATIC_CORRECTION_PER_SINGLE_CURRENCY_UNIT
 * </code>
 * </p>
 * <br>
 * <br>
 * <p>
 * <b>Input:</b>
 * <ul>
 *     <li><code>MortgageData</code> record containing:
 *         <ul>
 *             <li><code>borrowedAmount</code> - total principal (<code>B</code>)</li>
 *             <li><code>annualInterestRate</code> - annual interest rate as a percentage (<code>R</code>)</li>
 *             <li><code>numberOfYearsToPay</code> - loan term in years (<code>T</code>)</li>
 *         </ul>
 *     </li>
 * </ul>
 * </p>
 * <br>
 * <p>
 * <b>Output:</b>
 * <ul>
 *     <li>Returns <code>MONTHLY_MORTGAGE_PAYMENT</code> - estimated monthly payment calculated using the above approximation.</li>
 * </ul>
 * </p>
 */

public class MortgageCalculator {

    private final BigDecimal MONTHS_PER_YEAR = new BigDecimal("12");
    private final BigDecimal SINGLE_CURRENCY_UNIT = BigDecimal.ONE;
    private final BigDecimal MACLAURIN_SERIES_EXPANSION_COEFFICIENT = new BigDecimal("12");

    public BigDecimal calculateMonthlyMortgagePayment(MortgageData mortgageData) {
        final BigDecimal borrowedAmount = mortgageData.borrowedAmount();
        final BigDecimal annualInterestRate = convertPercentToDecimal(mortgageData.annualInterestRate());
        final BigDecimal numberOfYearsToPay = mortgageData.numberOfYearsToPay();
        final BigDecimal numberOfMonthlyPayments = numberOfYearsToPay.multiply(MONTHS_PER_YEAR);
        final BigDecimal total_repayment_per_single_currency_unit = getTotalRepaymentPerSingleCurrencyUnit(annualInterestRate, numberOfYearsToPay);

        return borrowedAmount
                .multiply(total_repayment_per_single_currency_unit)
                .divide(numberOfMonthlyPayments, 4, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getTotalRepaymentPerSingleCurrencyUnit(BigDecimal annualInterestRate, BigDecimal numberOfYearsToPay) {
        final BigDecimal linearInterestApproximationPerSingleCurrencyUnit = annualInterestRate
                .multiply(getAverageOf(numberOfYearsToPay, BigDecimal.ZERO));

        final BigDecimal quadraticCorrectionPerSingleCurrencyUnit = annualInterestRate
                .multiply(numberOfYearsToPay)
                .pow(2)
                .divide(MACLAURIN_SERIES_EXPANSION_COEFFICIENT, 4, RoundingMode.HALF_EVEN);

        return SINGLE_CURRENCY_UNIT
                        .add(linearInterestApproximationPerSingleCurrencyUnit)
                        .add(quadraticCorrectionPerSingleCurrencyUnit);
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
