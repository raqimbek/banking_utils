import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.andrylat.raqimbek.bankingutils.core.validators.CardValidator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ValidationHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    var outputStream = exchange.getResponseBody();

    if ("POST".equals(exchange.getRequestMethod())) {
      var cardNumber = Arrays.asList(readRequestBody(exchange).split(""));
      var cardValidator = new CardValidator();
      var cardValidationInfo = cardValidator.validate(cardNumber);

      if (cardValidationInfo.isValid()) {
        var response = String.valueOf(cardValidationInfo.isValid());
        exchange.sendResponseHeaders(200, response.length());
        outputStream.write(response.getBytes());
        outputStream.close();
      } else {
        var response = String.join("; ", cardValidationInfo.errors());
        exchange.sendResponseHeaders(400, response.length());
        outputStream.write(response.getBytes());
        outputStream.close();
      }
    }
  }

  private String readRequestBody(HttpExchange exchange) throws IOException {
    var requestBody = new StringBuilder();
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        requestBody.append(line);
      }
    }
    return requestBody.toString();
  }
}
