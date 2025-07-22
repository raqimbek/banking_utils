import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
  public static void main(String[] args) throws IOException {

    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

    server.createContext(
        "/card-number/validation",
        new HttpHandler() {
          @Override
          public void handle(HttpExchange exchange) throws IOException {}
        });

    server.createContext(
        "/card-number/payment-system",
        new HttpHandler() {
          @Override
          public void handle(HttpExchange exchange) throws IOException {}
        });

    server.createContext(
        "mortgage/calculation",
        new HttpHandler() {
          @Override
          public void handle(HttpExchange exchange) throws IOException {}
        });

    server.start();
    System.out.println("Server started on port 8000");
  }
}
