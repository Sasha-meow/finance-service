package oop.finance.exception;

/** Ошибка ненахождения пользователя */
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
