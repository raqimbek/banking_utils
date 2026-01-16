package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;


public class HttpRequestValidationFilter extends Filter {
    HttpResponder httpResponder = new HttpResponder();
    HttpRequestReader httpRequestReader = new HttpRequestReader();
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        var httpRequestValidationErrorOptional = validateRequest(exchange);

        httpRequestValidationErrorOptional.ifPresent(httpRequestValidationError -> {

            var message = httpRequestValidationError.errorMessage();
            var responseJson = new JSONObject().put("error-message", message);
            var statusCode = httpRequestValidationError.statusCode();

            try {
                if (statusCode == HttpResponseStatusCode.METHOD_NOT_ALLOWED) {
                    exchange.getResponseHeaders().set("Allow", "POST");
                }
                httpResponder.respondJson(exchange, responseJson, statusCode.getCode());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        chain.doFilter(exchange);
    }

    @Override
    public String description() { return "Request validation filter"; }

    private Optional<HttpRequestValidationError> validateRequest(HttpExchange exchange) throws IOException {
        var requestMethod = exchange.getRequestMethod();
        var contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        var requestBodyBytes = httpRequestReader.readRequestBodyBytes(exchange);
        var responseJson = new JSONObject();

        httpResponder.respondJson(exchange, responseJson, 400);

        if (!requestMethod.equalsIgnoreCase("POST")) {
            return Optional.of(
                    new HttpRequestValidationError(
                            "Only POST is allowed",
                            HttpResponseStatusCode.METHOD_NOT_ALLOWED));
        }

        if (contentType == null || !contentType.startsWith("application/json")) {
            return Optional.of(
                    new HttpRequestValidationError(
                            "Content-Type must be application/json",
                            HttpResponseStatusCode.UNSUPPORTED_MEDIA_TYPE));
        }

        if (requestBodyBytes.length == 0) {
            return Optional.of(
                    new HttpRequestValidationError(
                            "Invalid Length: Requested body must not be empty",
                            HttpResponseStatusCode.BAD_REQUEST));
        }
        return Optional.empty();
    }
}
