package dev.andrylat.raqimbek.bankingutils.core.services.paymentsystemdeterminer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.andrylat.raqimbek.bankingutils.core.services.paymentsystemdeterminer.PaymentSystemDeterminer;
import org.junit.jupiter.api.Test;

public class PaymentSystemDeterminerTest {
  PaymentSystemDeterminer paymentSystemDeterminer = new PaymentSystemDeterminer();

  //  @Test
  //  public void shouldReturnVisa() {
  //    var expected = "VISA";
  //    var cardNumber = "4263 9826 4026 9299";
  //    var actual = getPaymentSystem(cardNumber);
  //
  //    assertEquals(expected, actual);
  //  }
  //
  //  @Test
  //  public void shouldReturnMasterCard() {
  //    var expected = "MASTERCARD";
  //    var cardNumber = "5425 2334 3010 9903";
  //    var actual = getPaymentSystem(cardNumber);
  //
  //    assertEquals(expected, actual);
  //  }
  //
  //  @Test
  //  public void shouldReturnEmptyString() {
  //    var expected = "";
  //    var cardNumber = "5863 9826 4026 9299";
  //    var actual = getPaymentSystem(cardNumber);
  //
  //    assertEquals(expected, actual);
  //  }

  private String getPaymentSystem(String cardNumber) {
    return paymentSystemDeterminer.determinePaymentSystem(cardNumber).toString();
  }
}
