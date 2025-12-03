package dev.andrylat.raqimbek.bankingutils.core.cardutility.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CardValidatorTest {
  CardValidator cardValidator = new CardValidator();

  @Test
  public void shouldReturnInvalidLengthErrorMessage() {
    var expected = "Length should be 16 symbols";
    var actual = "";
    var errors = cardValidator.validate(new BigDecimal("1234")).errors();

    if (!errors.isEmpty()) {
      actual = errors.getFirst();
    }
    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnNoPaymentSystemErrorMessage() {
    var expected = "Payment System can't be determined";
    var actual = "";
    var errors = cardValidator.validate(new BigDecimal("5625233430109903")).errors();

    if (!errors.isEmpty()) {
      actual = errors.getFirst();
    }

    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnLuhnTestErrorMessage() {
    var expected ="Card Number does not pass the Luhn Test";
    var actual = cardValidator.validate(new BigDecimal("5425233430109923")).errors();

    assertTrue(actual.contains(expected));
  }

  @Test
  public void shouldReturnNoErrorMessage() {
    var expected = 0;
    var errors = cardValidator.validate(new BigDecimal("4333294581965034")).errors();
    var actual = errors.size();



    assertEquals(expected, actual);
  }
}
