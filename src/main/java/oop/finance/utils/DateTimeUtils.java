package oop.finance.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import oop.finance.exception.InvalidCredentialsException;

/** Утилитарный класс для работы с форматами дат. Предоставляет метод для парсинга и константы */
public class DateTimeUtils {
  public static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
  public static final String DATE_TIME_FORMAT_REPORT = "yyyy-MM-dd_HH-mm-ss";
  public static final String DATE_FORMAT = "dd.MM.yyyy";

  public static LocalDateTime parseStringDate(String str) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
      LocalDate date = LocalDate.parse(str, formatter);
      return date.atStartOfDay();
    } catch (DateTimeParseException e) {
      throw new InvalidCredentialsException("Неверный формат даты. Используйте дд.мм.гггг");
    }
  }
}
