package oop.finance.service.base;

import oop.finance.exception.InvalidCredentialsException;

/** Базовый класс сервиса. Предоставляет основные методы валидации данных */
public abstract class BaseService {
  // Проверяет, что объект не является null
  protected void validateNotNull(Object obj, String fieldName) {
    if (obj == null) {
      throw new InvalidCredentialsException(fieldName + " не может быть равен null!");
    }
  }

  // Проверяет, что числовое значение положительное
  protected void validatePositive(double value, String fieldName) {
    if (value <= 0) {
      throw new InvalidCredentialsException(fieldName + " должно быть положительным!");
    }
  }

  // Проверяет, что массив строк не пустой
  protected void validateNotEmpty(String[] arr, String fieldName) {
    if (arr.length == 0) {
      throw new InvalidCredentialsException(fieldName + " не может быть пустым!");
    }
  }
}
