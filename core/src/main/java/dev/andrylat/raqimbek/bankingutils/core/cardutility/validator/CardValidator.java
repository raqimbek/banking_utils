package dev.andrylat.raqimbek.bankingutils.core.cardutility.validator;

import dev.andrylat.raqimbek.bankingutils.core.cardutility.service.paymentsystemdeterminer.PaymentSystem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CardValidator implements Validator<BigDecimal> {
    public CardValidationResult validate(BigDecimal cardNumber) {
        var errors = generateErrors(cardNumber);

        return new CardValidationResult(errors.isEmpty(), errors);
    }

    private List<String> generateErrors(BigDecimal cardNumber) {
        var errors = new ArrayList<String>();
        var validLength = PaymentSystem.CARD_VALID_LENGTH;
        var hasValidLength = hasValidLength(cardNumber, validLength);
        var hasValidPrefix = hasValidPrefix(cardNumber);
        var passesLuhnTest = passesLuhnTest(parseCardNumber(cardNumber));

        if (!hasValidLength) {
            errors.add(
                    new StringBuilder("Length should be ").append(validLength).append(" symbols").toString());
        }

        if (!hasValidPrefix) {
            errors.add("Payment System can't be determined");
        }

        if (!passesLuhnTest) {
            errors.add("Card Number does not pass the Luhn Test");
        }

        return errors;
    }

    private boolean hasValidPrefix(BigDecimal cardNumber) {
        var hasValidPrefix = false;

        for (PaymentSystem paymentSystem : PaymentSystem.values()) {
            var prefixes = paymentSystem.getPrefixes();

            hasValidPrefix = prefixes.stream().anyMatch(p -> cardNumber.toString().startsWith(p.toString()));

            if (hasValidPrefix) {
                return true;
            }
        }

        return false;
    }

    private boolean hasValidLength(BigDecimal cardNumber, int validLength) {
        var cardNumberLength = cardNumber.toString().length();
        return cardNumberLength == validLength;
    }

    private boolean passesLuhnTest(List<Integer> cardNumberAsList) {
        var everyOtherNumberList =
                new ArrayList<>(
                        IntStream.range(0, cardNumberAsList.size())
                                .filter(n -> n % 2 == 0)
                                .mapToObj(cardNumberAsList::get)
                                .toList());

        var numbersWithTwoDigits =
                everyOtherNumberList.stream()
                        .map(n -> n * 2)
                        .filter(n -> n >= 10 && n < 100)
                        .map(n -> n / 2)
                        .toList();

        for (var i = 0; i < cardNumberAsList.size(); i++) {
            if (everyOtherNumberList.contains(cardNumberAsList.get(i))) {
                cardNumberAsList.remove(cardNumberAsList.get(i));
            }
        }

        for (var i = 0; i < everyOtherNumberList.size(); i++) {
            if (numbersWithTwoDigits.contains(everyOtherNumberList.get(i))) {
                everyOtherNumberList.remove(everyOtherNumberList.get(i));
                i--;
            }
        }

        var sumOfNumbersWithTwoDigits =
                numbersWithTwoDigits.stream()
                        .map(n -> n * 2)
                        .map(String::valueOf)
                        .map(
                                s ->
                                        Arrays.stream(s.split(""))
                                                .map(Integer::valueOf)
                                                .mapToInt(Integer::intValue)
                                                .sum())
                        .mapToInt(Integer::intValue)
                        .sum();

        var sumOfEveryOtherNumber =
                everyOtherNumberList.stream().map(n -> n * 2).mapToInt(Integer::intValue).sum();

        var cardNumberSum = cardNumberAsList.stream().mapToInt(Integer::intValue).sum();

        var sum = cardNumberSum + sumOfNumbersWithTwoDigits + sumOfEveryOtherNumber;

        return sum % 10 == 0;
    }

    private List<Integer> parseCardNumber(BigDecimal cardNumber) {
        return stringToCharactersList(cardNumber.toString()).stream()
                .filter(Character::isDigit)
                .map(Character::getNumericValue)
                .collect(Collectors.toList());
    }

    private List<Character> stringToCharactersList(String str) {
        return Arrays.stream(str.split("")).map(s -> s.charAt(0)).collect(Collectors.toList());
    }
}
