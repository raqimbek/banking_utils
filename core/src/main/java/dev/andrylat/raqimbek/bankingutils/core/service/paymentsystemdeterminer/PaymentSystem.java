package dev.andrylat.raqimbek.bankingutils.core.service.paymentsystemdeterminer;

import java.util.List;
import java.util.ArrayList;

public enum PaymentSystem {
  VISA(List.of(4)),
  MASTERCARD(List.of(51, 52, 53, 54, 55)),
  DISCOVER(List.of(6011, 65)),
  DINERS_CLUB(List.of(36, 38)),
  JCB(List.of(35)),
  AMERICAN_EXPRESS(List.of(34, 37));

  public static final int CARD_VALID_LENGTH = 16;
  private final List<Integer> prefixList = new ArrayList<>();

  PaymentSystem(List<Integer> prefixes) {
    prefixList.addAll(prefixes);
  }

  public List<Integer> getPrefixes() {
    return prefixList;
  }
}
