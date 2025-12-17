package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageCalculator;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageData;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.validator.MortgageDataValidator;
import org.json.JSONObject;
import java.io.IOException;

public class MortgageCalculationHandler implements HttpHandler {
  MortgageCalculator mortgageCalculator = new MortgageCalculator();
  MortgageDataValidator mortgageDataValidator = new MortgageDataValidator();
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
        var mortgageData = new MortgageData(requestJson.getBigDecimal("borrowedAmount"),requestJson.getBigDecimal("annualInterest"),requestJson.getBigDecimal("numberOfYearsToPay"));
        var mortgageInputValidationResult =
            mortgageDataValidator.validate(mortgageData);
        var response = new JSONObject();

        if (mortgageInputValidationResult.isValid()) {
          response.put(
              "monthly-mortgage-payment-amount",
              mortgageCalculator
                  .calculateMonthlyMortgagePayment(mortgageData));
          httpResponder.respondJson(exchange, response, 200);
        } else {
          response.put("validation-messages", mortgageInputValidationResult.errors());
          httpResponder.respondJson(exchange, response, 400);
        }
      }
    }
  }
}
