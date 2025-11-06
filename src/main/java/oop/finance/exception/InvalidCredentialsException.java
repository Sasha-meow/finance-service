package oop.finance.exception;

/** Ошибка некорректных входных данных */
public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException(String message) {
    super(message);
  }
}
