package dev.andrylat.raqimbek.bankingutils.core.cardutility.service.paymentsystemdeterminer;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentSystemDeterminerTest {
    PaymentSystemDeterminer paymentSystemDeterminer = new PaymentSystemDeterminer();

    @Test
    public void shouldReturnVisa() {
        var expectedPaymentSystem = "VISA";
        var cardNumber = new BigDecimal("4263982640269299");

        assertPaymentSystem(cardNumber, expectedPaymentSystem);
    }

    @Test
    public void shouldReturnMasterCard() {
        var expectedPaymentSystem = "MASTERCARD";
        var cardNumber = new BigDecimal("5425233430109903");

        assertPaymentSystem(cardNumber, expectedPaymentSystem);
    }

    @Test
    public void shouldReturnEmptyOptional() {
        var cardNumber = new BigDecimal("5863982640269299");

        assertFalse(paymentSystemDeterminer.determinePaymentSystem(cardNumber).isPresent());

    }

    private void assertPaymentSystem(BigDecimal cardNumber, String expectedPaymentSystem) {
        var optionalPaymentSystem = paymentSystemDeterminer.determinePaymentSystem(cardNumber);

        assertTrue(optionalPaymentSystem.isPresent());
        assertEquals(expectedPaymentSystem, optionalPaymentSystem.get().toString());
    }
}
