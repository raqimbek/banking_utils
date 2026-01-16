package dev.andrylat.raqimbek.bankingutils.core.cardutility.validator;


import java.util.List;

public interface Validator<T> {
  List<String> validate(T input);
}
