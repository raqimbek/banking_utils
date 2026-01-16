package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageCalculator;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageData;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.validator.MortgageDataValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;


public class MortgageCalculationHandler extends BankingHandler implements HttpHandler  {
    private final MortgageCalculator mortgageCalculator;
    private final MortgageDataValidator mortgageDataValidator;
    private final HttpResponder httpResponder;

    public MortgageCalculationHandler(MortgageCalculator mortgageCalculator,
                                      MortgageDataValidator mortgageDataValidator,
                                      HttpRequestReader httpRequestReader,
                                      HttpResponder httpResponder) {
        super(httpRequestReader, httpResponder);
        this.mortgageCalculator = mortgageCalculator;
        this.mortgageDataValidator = mortgageDataValidator;
        this.httpResponder = httpResponder;
    }

    protected void handleRequest(
            JSONObject requestBodyJson,
            JSONObject responseJson,
            HttpExchange exchange) throws IOException {
            var mortgageData = new MortgageData(
                    requestBodyJson.getBigDecimal("borrowedAmount"),
                    requestBodyJson.getBigDecimal("annualInterest"),
                    requestBodyJson.getBigDecimal("numberOfYearsToPay"));

            var mortgageInputValidationErrors =
                    mortgageDataValidator.validate(mortgageData);

            if (!mortgageInputValidationErrors.isEmpty()) {
                responseJson.put("errors", new JSONArray(mortgageInputValidationErrors));
                httpResponder.respondJson(exchange, responseJson, 400);
                return;
            }

            responseJson.put(
                    "monthlyMortgagePayment",
                    mortgageCalculator
                            .calculateMonthlyMortgagePayment(mortgageData));
            httpResponder.respondJson(exchange, responseJson, 200);
    }
}
