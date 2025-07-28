package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.services.paymentsystemdeterminer.PaymentSystemDeterminer;
import dev.andrylat.raqimbek.bankingutils.core.validators.CardValidator;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PaymentSystemHandler implements HttpHandler {
  CardValidator cardValidator = new CardValidator();
  PaymentSystemDeterminer paymentSystemDeterminer = new PaymentSystemDeterminer();
  HttpRequestReader httpRequestReader = new HttpRequestReader();
  HttpResponder httpResponder = new HttpResponder();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    var contentType = exchange.getRequestHeaders().getFirst("Content-Type");

    if (exchange.getRequestMethod().equals("POST")) {
      if (contentType != null && contentType.startsWith("application/x-www-form-urlencoded")) {
        var requestParametersMap = httpRequestReader.getRequestBodyParametersMap(exchange);
        var requestJson = new JSONObject();
        requestParametersMap.forEach(requestJson::put);

        var cardNumber = requestJson.getString("cardNumber");
        var cardValidationInfo = cardValidator.validate(List.of(cardNumber));
        var response = new JSONObject();

        if (cardValidationInfo.isValid()) {
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
                  Map.of("validation-messages", cardValidationInfo.errors())));
          httpResponder.respondJson(exchange, response, 400);
        }
      }
    }
  }
}
