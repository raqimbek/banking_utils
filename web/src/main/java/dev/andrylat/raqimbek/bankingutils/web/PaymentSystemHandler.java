package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.service.paymentsystemdeterminer.PaymentSystemDeterminer;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.validator.CardValidator;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PaymentSystemHandler implements HttpHandler {
    private CardValidator cardValidator;
    private PaymentSystemDeterminer paymentSystemDeterminer;
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

                try (exchange) {
                    if (cardValidationResult.isValid()) {
                        var paymentSystemOptional = paymentSystemDeterminer.determinePaymentSystem(cardNumber);

                        if (paymentSystemOptional.isPresent()) {
                            response.put("is-payment-system-determination-successful", true);
                            response.put("payment-system", paymentSystemOptional.get().toString());
                            httpResponder.respondJson(exchange, response, 200);
                        } else {
                            response.put("is-payment-system-determination-successful", false);
                            response.put(
                                    "errors", "Something went wrong... Payment system could not be determined.");
                            httpResponder.respondJson(exchange, response, 400);
                        }
                    } else {
                        response.put("is-payment-system-determination-successful", false);
                        response.put(
                                "errors",
                                List.of(
                                        Map.of("validation-result", false),
                                        Map.of("validation-messages", cardValidationResult.errors())));
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
