package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.service.paymentsystemdeterminer.PaymentSystemDeterminer;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.validator.CardValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class CardValidationHandler extends BankingHandler implements HttpHandler {
    private final PaymentSystemDeterminer paymentSystemDeterminer;
    private final CardValidator cardValidator;
    private final HttpResponder httpResponder;

    public CardValidationHandler(PaymentSystemDeterminer paymentSystemDeterminer,
                                 CardValidator cardValidator,
                                 HttpRequestReader httpRequestReader,
                                 HttpResponder httpResponder) {
        super(httpRequestReader, httpResponder);
        this.paymentSystemDeterminer = paymentSystemDeterminer;
        this.cardValidator = cardValidator;
        this.httpResponder = httpResponder;
    }

    protected void handleRequest(
            JSONObject requestBodyJson,
            JSONObject responseJson,
            HttpExchange exchange) throws IOException {

            var cardNumber = requestBodyJson.getBigDecimal("cardNumber");
            var cardValidationErrors = cardValidator.validate(cardNumber);

            responseJson.put("isCardValid", cardValidationErrors.isEmpty());
            cardValidationErrors.forEach(cardValidationError ->
                    responseJson.getJSONArray("errors").put(cardValidationError));

            if (!cardValidationErrors.isEmpty()) {
                httpResponder.respondJson(exchange, responseJson, 400);
                return;
            }

            var paymentSystemOptional = paymentSystemDeterminer.determinePaymentSystem(cardNumber);

            if (paymentSystemOptional.isEmpty()) {
                responseJson.getJSONArray("errors")
                        .put("Something went wrong... Payment system could not be determined.");
                httpResponder.respondJson(exchange, responseJson, 400);
                return;
            }

            responseJson.put("paymentSystem", paymentSystemOptional.get().toString());

            httpResponder.respondJson(exchange, responseJson, 200);
    }

    @Override
    protected void initializeResponse(JSONObject responseJson) {
        responseJson.put("errors", new JSONArray());
    }

    @Override
    protected void handleJsonException(JSONObject responseJson) {
        responseJson.put("isCardValid", false);
        super.handleJsonException(responseJson);
    }
}
