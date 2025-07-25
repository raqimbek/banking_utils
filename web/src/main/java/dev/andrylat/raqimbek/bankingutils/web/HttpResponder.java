package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;

public class HttpResponder {
  private void respond(HttpExchange exchange, String response, int statusCode) throws IOException {
    var outputStream = exchange.getResponseBody();

    exchange.sendResponseHeaders(statusCode, response.length());

    outputStream.write(response.getBytes());
    outputStream.close();
  }

  public void respondJson(HttpExchange exchange, JSONObject responseDataJson, int statusCode)
      throws IOException {
    exchange.getResponseHeaders().set("Content-Type", "application/json");

    var responseJson = new JSONObject();
    responseJson.put("status", 200);
    responseJson.put("message", "OK");

    responseDataJson.keySet().forEach(key -> responseJson.put(key, responseDataJson.get(key)));

    respond(exchange, responseJson.toString(), statusCode);
  }
}
