package dev.andrylat.raqimbek.bankingutils.web;

public record HttpRequestValidationError(
        String errorMessage,
        HttpResponseStatusCode statusCode
) {
}
