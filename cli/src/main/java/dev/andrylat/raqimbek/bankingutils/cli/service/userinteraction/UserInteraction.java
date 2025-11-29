package dev.andrylat.raqimbek.bankingutils.cli.service.userinteraction;

import java.math.BigDecimal;
import java.util.List;

public interface UserInteraction {

  void write(String message);

  void writeAll(List<String> messages);

  String read();

  BigDecimal readBigDecimal();
}
