package dev.andrylat.raqimbek.bankingutils.core.cardutility.validator;

import java.util.List;

public interface ValidationResult {
  boolean isValid();

  List<String> errors();
}
