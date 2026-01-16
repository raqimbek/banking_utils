package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class HttpRequestReader {
  public byte[] readRequestBodyBytes(HttpExchange exchange) throws IOException {
    return exchange.getRequestBody().readAllBytes();
  }

  public String bytesToString(byte[] bytes) {
    return new String(bytes, StandardCharsets.UTF_8);
  }

  public JSONObject getRequestAsJson(byte[] requestBodyBytes) {
    return new JSONObject(bytesToString(requestBodyBytes));
  }
}
