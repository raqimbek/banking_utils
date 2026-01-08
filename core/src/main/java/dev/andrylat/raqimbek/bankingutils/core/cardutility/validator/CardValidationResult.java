package dev.andrylat.raqimbek.bankingutils.core.cardutility.validator;

import java.util.List;

public record CardValidationResult(
        boolean isValid,
        List<String> errors) implements ValidationResult {}
