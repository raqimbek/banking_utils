package dev.andrylat.raqimbek.bankingutils.core.validators;


public interface Validator<T> {
  ValidationInfo validate(T input);
}
