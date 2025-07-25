package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.validators.CardValidator;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;

public class CardValidationHandler implements HttpHandler {
  CardValidator cardValidator = new CardValidator();
  HttpRequestReader httpRequestReader = new HttpRequestReader();
  HttpResponder httpResponder = new HttpResponder();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if ("POST".equals(exchange.getRequestMethod())) {
      var requestBody = httpRequestReader.readRequestBody(exchange);
      var requestJson = new JSONObject(requestBody);
      var cardNumber = Arrays.asList(requestJson.getString("cardNumber").split(""));
      var cardValidationInfo = cardValidator.validate(cardNumber);
      var response = new JSONObject();

      response.put("validation-result", cardValidationInfo.isValid());

      if (cardValidationInfo.isValid()) {
        httpResponder.respondJson(exchange, response, 200);
      } else {
        response.put("validation-messages", cardValidationInfo.errors());
        httpResponder.respondJson(exchange, response, 400);
      }
    }
  }
}
