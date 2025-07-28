package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.services.mortgagecalculator.MortgageCalculator;
import dev.andrylat.raqimbek.bankingutils.core.validators.MortgageInputValidator;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

public class MortgageCalculationHandler implements HttpHandler {
  MortgageCalculator mortgageCalculator = new MortgageCalculator();
  MortgageInputValidator mortgageInputValidator = new MortgageInputValidator();
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
        var borrowedAmount = requestJson.getString("borrowedAmount");
        var annualInterest = requestJson.getString("annualInterest");
        var numberOfYears = requestJson.getString("numberOfYears");
        var mortgageInputValidationInfo =
            mortgageInputValidator.validate(List.of(borrowedAmount, annualInterest, numberOfYears));
        var response = new JSONObject();

        if (mortgageInputValidationInfo.isValid()) {
          response.put(
              "monthly-mortgage-payment-amount",
              mortgageCalculator
                  .calculateMonthlyMortgagePayment(
                      List.of(borrowedAmount, annualInterest, numberOfYears))
                  .toString());
          httpResponder.respondJson(exchange, response, 200);
        } else {
          response.put("validation-messages", mortgageInputValidationInfo.errors());
          httpResponder.respondJson(exchange, response, 400);
        }
      }
    }
  }
}
