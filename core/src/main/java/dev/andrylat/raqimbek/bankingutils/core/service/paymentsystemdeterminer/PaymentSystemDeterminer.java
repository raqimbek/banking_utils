package dev.andrylat.raqimbek.bankingutils.core.service.paymentsystemdeterminer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

public class PaymentSystemDeterminer {
  public Optional<PaymentSystem> determinePaymentSystem(BigDecimal cardNumber) {
    return Arrays.stream(PaymentSystem.values())
        .filter(
            p ->
                p.getPrefixes().stream()
                    .anyMatch(prefix -> cardNumber.toString().startsWith(prefix.toString())))
        .findFirst();
  }
}
