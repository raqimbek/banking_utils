package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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

  public Map<String, String> getRequestBodyParametersMap(HttpExchange exchange) throws IOException {

    var requestBodyString = readRequestBody(exchange);

    var parameterArray = requestBodyString.split("&");
    var parameterList = new ArrayList<List<String>>();
    Arrays.stream(parameterArray).forEach(s -> parameterList.add(Arrays.asList(s.split("="))));

    var parameterMap = new HashMap<String, String>();
    parameterList.forEach(p -> parameterMap.put(p.getFirst(), p.get(1)));

    return parameterMap;
  }
}
