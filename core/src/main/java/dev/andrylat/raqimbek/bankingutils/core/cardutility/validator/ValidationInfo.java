package dev.andrylat.raqimbek.bankingutils.core.cardutility.validator;

import java.util.List;

public interface ValidationInfo {
  boolean isValid();

  List<String> errors();
}
