package dev.andrylat.raqimbek.bankingutils.core.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

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
    var expected = List.of("Card Number does not pass the Luhn Test");
    var actual = cardValidator.validate(new BigDecimal("5425 2334 3010 9923")).errors();

    assertEquals(expected, actual);
  }

  @Test
  public void shouldReturnNoErrorMessage() {
    var expected = 0;
    var errors = cardValidator.validate(new BigDecimal("5425 2334 3010 9903")).errors();
    var actual = errors.size();

    assertEquals(expected, actual);
  }
}
