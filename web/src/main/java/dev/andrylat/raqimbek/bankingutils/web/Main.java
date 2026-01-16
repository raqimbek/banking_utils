package dev.andrylat.raqimbek.bankingutils.web;

import com.sun.net.httpserver.HttpServer;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.service.paymentsystemdeterminer.PaymentSystemDeterminer;
import dev.andrylat.raqimbek.bankingutils.core.cardutility.validator.CardValidator;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.service.MortgageCalculator;
import dev.andrylat.raqimbek.bankingutils.core.mortgageutility.validator.MortgageDataValidator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;

public class Main {
    private static final MortgageCalculator mortgageCalculator = new MortgageCalculator();
    private static final MortgageDataValidator mortgageDataValidator = new MortgageDataValidator();
    private static final CardValidator cardValidator = new CardValidator();
    private static final PaymentSystemDeterminer paymentSystemDeterminer = new PaymentSystemDeterminer();
    private static final HttpResponder httpResponder = new HttpResponder();
    private static final HttpRequestReader httpRequestReader = new HttpRequestReader();

    public static void main(String[] args) {

        var port = 0;

        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext(
                            "/card-number/validation",
                            new CardValidationHandler(
                                    paymentSystemDeterminer,
                                    cardValidator,
                                    httpRequestReader,
                                    httpResponder))
                    .getFilters()
                    .add(new HttpRequestValidationFilter());
            server.createContext("/mortgage/calculation",
                    new MortgageCalculationHandler(
                            mortgageCalculator,
                            mortgageDataValidator,
                            httpRequestReader,
                            httpResponder));

            server.start();
            System.out.println("Server started on port " + port);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            System.err.println("Usage: java -jar bankingutils.jar [port]");
        } catch (IOException | UncheckedIOException e) {
            System.err.println("Something went wrong... Detected errors:");
            System.err.println(e.getMessage());
        }
    }
}
