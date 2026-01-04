package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageCalculator;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageData;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.validator.MortgageDataValidator;
import lombok.AllArgsConstructor;
import org.json.JSONObject;

import java.io.IOException;

@AllArgsConstructor
public class MortgageCalculationHandler implements HttpHandler {
    private MortgageCalculator mortgageCalculator;
    private MortgageDataValidator mortgageDataValidator;
    private HttpRequestReader httpRequestReader;
    private HttpResponder httpResponder;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        var response = new JSONObject();

        if (exchange.getRequestMethod().equals("POST")) {
            if (contentType != null && contentType.startsWith("application/json")) {
                var mortgageData = new MortgageData(
                        httpRequestReader.getRequestParameterAsBigDecimal("borrowedAmount", exchange),
                        httpRequestReader.getRequestParameterAsBigDecimal("annualInterest", exchange),
                        httpRequestReader.getRequestParameterAsBigDecimal("numberOfYearsToPay", exchange));
                var mortgageInputValidationResult =
                        mortgageDataValidator.validate(mortgageData);

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
