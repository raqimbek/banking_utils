package dev.andrylat.raqimbek.bankingutils.core.validator;

import java.util.List;

public record MortgageDataValidationInfo(boolean isValid, List<String> errors)
    implements ValidationInfo {}
