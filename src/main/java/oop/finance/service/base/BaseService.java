package oop.finance.service.base;

import oop.finance.exception.InvalidCredentialsException;

public abstract class BaseService {
  protected void validateNotNull(Object obj, String fieldName) {
    if (obj == null) {
      throw new InvalidCredentialsException(fieldName + " не может быть равен null!");
    }
  }

  protected void validatePositive(double value, String fieldName) {
    if (value <= 0) {
      throw new InvalidCredentialsException(fieldName + " не может быть отрицательным!");
    }
  }

  protected void validateNotEmpty(String[] arr, String fieldName) {
    if (arr.length == 0) {
      throw new InvalidCredentialsException(fieldName + " не может быть пустым!");
    }
  }
}
