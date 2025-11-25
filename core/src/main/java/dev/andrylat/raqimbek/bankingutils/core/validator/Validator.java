package dev.andrylat.raqimbek.bankingutils.core.validator;


public interface Validator<T> {
  ValidationInfo validate(T input);
}
