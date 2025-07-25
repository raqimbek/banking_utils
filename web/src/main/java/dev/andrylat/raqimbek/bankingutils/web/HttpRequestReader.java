package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpRequestReader {
  public String readRequestBody(HttpExchange exchange) throws IOException {
    var requestBody = new StringBuilder();
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
      var line = "";
      while ((line = reader.readLine()) != null) {
        requestBody.append(line);
      }
    }
    return requestBody.toString();
  }
}
