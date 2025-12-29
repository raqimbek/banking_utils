package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.validator.CardValidator;
import org.json.JSONObject;
import java.io.IOException;

public class CardValidationHandler implements HttpHandler {
  CardValidator cardValidator = new CardValidator();
  HttpRequestReader httpRequestReader = new HttpRequestReader();
  HttpResponder httpResponder = new HttpResponder();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    var contentType = exchange.getRequestHeaders().getFirst("Content-Type");

    if (exchange.getRequestMethod().equals("POST")) {
      if (contentType != null && contentType.startsWith("application/json")) {
        var requestParametersMap = httpRequestReader.getRequestBodyParametersMap(exchange);
        var requestJson = new JSONObject();
        requestParametersMap.forEach(requestJson::put);
        var cardNumber = requestJson.getBigDecimal("cardNumber");
        var cardValidationResult = cardValidator.validate(cardNumber);
        var response = new JSONObject();

        response.put("validation-result", cardValidationResult.isValid());

        if (cardValidationResult.isValid()) {
          httpResponder.respondJson(exchange, response, 200);
        } else {
          response.put("validation-messages", cardValidationResult.errors());
          httpResponder.respondJson(exchange, response, 400);
        }
      }
    }
  }
}
