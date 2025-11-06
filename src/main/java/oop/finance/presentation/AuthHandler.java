package oop.finance.presentation;

import oop.finance.exception.InvalidCredentialsException;
import oop.finance.exception.UserNotFoundException;
import oop.finance.presentation.base.BaseUI;
import oop.finance.service.AuthService;

/** Класс-обработчик пользовательского интерфейса для работы с авторизацией. */
public class AuthHandler extends BaseUI {
  private final AuthService authService;

  public AuthHandler(AuthService authService) {
    this.authService = authService;
  }

  public void showAndHandleAuthMenu() {
    showAuthMenu();
    handleAuthMenu();
  }

  // Отображает меню авторизации
  private void showAuthMenu() {
    System.out.println("1 - Войти по логину и паролю");
    System.out.println("2 - Зарегистрироваться");
    System.out.println("0 - Завершить работу");
    System.out.println("help - Справка");
  }

  // Обрабатывает выбор из меню авторизации
  private void handleAuthMenu() {
    String choice = readString("Выберите нужную опцию: ");

    switch (choice) {
      case "0":
        handleExit();
        break;
      case "1":
        handleLogin();
        break;
      case "2":
        handleRegister();
        break;
      case "help":
        showHelp();
        break;
      default:
        printErrorNotification("Некорректная опция!");
    }
  }

  // Обрабатывает данные авторизации
  private void handleLogin() {
    try {
      String login = readString("Введите логин: ");
      String password = readString("Введите пароль: ");

      authService.authenticate(login, password);
      printSuccessNotification("Успешный вход! Привет, " + authService.getCurrentUser().getLogin());
    } catch (IllegalArgumentException | UserNotFoundException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка авторизации: " + error.getMessage());
    }
  }

  // Обрабатывает данные регистрации
  private void handleRegister() {
    try {
      String login = readString("Придумайте логин: ");
      String password = readString("Придумайте пароль: ");

      authService.register(login, password);
      printSuccessNotification("Успешная регистрация!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка регистрации: " + error.getMessage());
    }
  }

  // Завершение работы в меню авторизации
  private void handleExit() {
    System.out.println("До скорых встреч!");
    scanner.close();
    System.exit(0);
  }
}
