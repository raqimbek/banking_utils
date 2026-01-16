package dev.andrylat.raqimbek.bankingutils.web;

public enum HttpResponseStatusCode {
    OK(200),
    BAD_REQUEST(400),
    METHOD_NOT_ALLOWED(405),
    UNSUPPORTED_MEDIA_TYPE(415);

    private final int STATUS_CODE;

    HttpResponseStatusCode(int statusCode) {
        STATUS_CODE = statusCode;
    }

    public int getCode() {
        return STATUS_CODE;
    }
}
