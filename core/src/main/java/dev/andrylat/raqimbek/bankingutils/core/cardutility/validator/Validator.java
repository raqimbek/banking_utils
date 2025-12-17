package dev.andrylat.raqimbek.bankingutils.core.cardutility.validator;


public interface Validator<T> {
  ValidationResult validate(T input);
}
