package dev.andrylat.raqimbek.bankingutils.core.mortgageutility.validator;

import dev.andrylat.raqimbek.bankingutils.core.cardutility.validator.ValidationInfo;

import java.util.List;

public record MortgageDataValidationResult(boolean isValid, List<String> errors)
    implements ValidationInfo {}
