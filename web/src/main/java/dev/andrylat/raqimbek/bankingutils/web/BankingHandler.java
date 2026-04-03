package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

@AllArgsConstructor
public abstract class BankingHandler implements HttpHandler {
    protected HttpRequestReader httpRequestReader;
    protected HttpResponder httpResponder;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var requestBodyBytes = httpRequestReader.readRequestBodyBytes(exchange);
        var responseJson = new JSONObject();
        initializeResponse(responseJson);

        try (exchange) {
            var requestBodyJson = httpRequestReader.getRequestAsJson(requestBodyBytes);
            handleRequest(requestBodyJson, responseJson, exchange);
        } catch (JSONException e) {
            handleJsonException(responseJson);
            httpResponder.respondJson(exchange, responseJson, 400);
        }
    }

    protected void initializeResponse(JSONObject responseJson) {}

    protected void handleJsonException(JSONObject responseJson) {
        var errorMessages = new JSONArray()
                .put("Provided JSON must be well-formatted and contain the correct keys");
        responseJson.put("errors", errorMessages);
    }

    protected abstract void handleRequest(
            JSONObject requestBodyJson,
            JSONObject responseJson,
            HttpExchange exchange) throws IOException;
}
