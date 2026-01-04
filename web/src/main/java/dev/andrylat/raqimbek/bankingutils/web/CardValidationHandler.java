package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.validator.CardValidator;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import java.io.IOException;

@AllArgsConstructor
public class CardValidationHandler implements HttpHandler {
    private CardValidator cardValidator;
    private HttpRequestReader httpRequestReader;
    private HttpResponder httpResponder;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        var response = new JSONObject();

        if (exchange.getRequestMethod().equals("POST")) {
            if (contentType != null && contentType.startsWith("application/json")) {
                var cardNumber = httpRequestReader.getRequestParameterAsBigDecimal("cardNumber", exchange);
                var cardValidationResult = cardValidator.validate(cardNumber);

                response.put("validation-result", cardValidationResult.isValid());

                try (exchange) {
                    if (cardValidationResult.isValid()) {
                        httpResponder.respondJson(exchange, response, 200);
                    } else {
                        response.put("validation-messages", cardValidationResult.errors());
                        httpResponder.respondJson(exchange, response, 400);
                    }
                }
            } else {
                response.put("errorMessage", "Content-Type must be application/json");
                httpResponder.respondJson(exchange, response, 415);
            }
        } else {
            exchange.getResponseHeaders().set("Allow", "POST");
            response.put("errorMessage", "Only POST is allowed");
            httpResponder.respondJson(exchange, response, 405);
        }
    }
}
