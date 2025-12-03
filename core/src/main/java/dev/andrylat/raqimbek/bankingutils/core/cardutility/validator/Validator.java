package dev.andrylat.raqimbek.bankingutils.core.cardutility.validator;


public interface Validator<T> {
  ValidationInfo validate(T input);
}
