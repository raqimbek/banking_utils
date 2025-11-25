package dev.andrylat.raqimbek.bankingutils.core.validator;

import dev.andrylat.raqimbek.bankingutils.core.service.paymentsystemdeterminer.PaymentSystem;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CardValidator implements Validator<List<String>> {
  public CardValidationInfo validate(List<String> inputList) {
    var errors = new ArrayList<String>();

    inputList.forEach(input -> errors.addAll(generateErrors(input)));

    return new CardValidationInfo(errors.isEmpty(), errors);
  }

  private List<String> generateErrors(String cardNumber) {
    var errors = new ArrayList<String>();

    cardNumber = removeWhiteSpace(cardNumber);

    if (!containsOnlyDigits(cardNumber)) {
      errors.add("Card Number must contain only digits");
    }

    var validLength = PaymentSystem.CARD_VALID_LENGTH;
    var hasValidLength = hasValidLength(cardNumber, validLength);

    if (!hasValidLength) {
      errors.add(
          new StringBuilder("Length should be ").append(validLength).append(" symbols").toString());
    }

    var hasValidPrefix = false;

    for (PaymentSystem paymentSystem : PaymentSystem.values()) {
      hasValidPrefix = hasValidPrefix(cardNumber, paymentSystem);

      if (hasValidPrefix) break;
    }

    if (!hasValidPrefix) {
      errors.add("Payment System can't be determined");
    }

    if (containsOnlyDigits(cardNumber) && !passesLuhnTest(parseCardNumber(cardNumber))) {
      errors.add("Card Number does not pass the Luhn Test");
    }

    return errors;
  }

  private boolean hasValidPrefix(String cardNumber, PaymentSystem paymentSystem) {
    var prefixes = paymentSystem.getPrefixes();

    return prefixes.stream().anyMatch(p -> cardNumber.startsWith(p.toString()));
  }

  private boolean hasValidLength(String cardNumber, int validLength) {
    var cardNumberLength = cardNumber.length();
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

  private List<Integer> parseCardNumber(String str) {
    return stringToCharactersList(str).stream()
        .filter(Character::isDigit)
        .map(Character::getNumericValue)
        .collect(Collectors.toList());
  }

  private List<Character> stringToCharactersList(String str) {
    return Arrays.stream(str.split("")).map(s -> s.charAt(0)).collect(Collectors.toList());
  }

  private String removeWhiteSpace(String s) {
    return s.replaceAll(" ", "");
  }

  private boolean containsOnlyDigits(String str) {
    return stringToCharactersList(str).stream().allMatch(Character::isDigit);
  }
}
