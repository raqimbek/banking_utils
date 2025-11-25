package dev.andrylat.raqimbek.bankingutils.core.service.paymentsystemdeterminer;

import java.util.Arrays;
import java.util.Optional;

public class PaymentSystemDeterminer {
  public Optional<PaymentSystem> determinePaymentSystem(String cardNumber) {
    return Arrays.stream(PaymentSystem.values())
        .filter(
            p ->
                p.getPrefixes().stream()
                    .anyMatch(prefix -> cardNumber.startsWith(prefix.toString())))
        .findFirst();
  }
}
