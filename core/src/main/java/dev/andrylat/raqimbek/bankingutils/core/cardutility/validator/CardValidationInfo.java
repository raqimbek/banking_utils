package dev.andrylat.raqimbek.bankingutils.core.cardutility.validator;

import java.util.List;

public record CardValidationInfo(boolean isValid, List<String> errors) implements ValidationInfo {}
