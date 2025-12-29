package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpResponder {
  private void respond(HttpExchange exchange, String response, int statusCode) throws IOException {
    var outputStream = exchange.getResponseBody();

    exchange.sendResponseHeaders(statusCode, response.length());

    outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    outputStream.close();
  }

  public void respondJson(HttpExchange exchange, JSONObject responseDataJson, int statusCode)
      throws IOException {

    exchange.getResponseHeaders().set("Content-Type", "application/json");

    respond(exchange, responseDataJson.toString(), statusCode);
  }
}
