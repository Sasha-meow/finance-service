package oop.finance.presentation;

import oop.finance.exception.InvalidCredentialsException;
import oop.finance.model.User;
import oop.finance.model.Wallet;
import oop.finance.presentation.base.BaseUI;
import oop.finance.service.AuthService;
import oop.finance.service.BudgetService;

/** Класс-обработчик пользовательского интерфейса для работы с бюджетами. */
public class BudgetHandler extends BaseUI {
  private final AuthService authService;
  private final BudgetService budgetService;

  public BudgetHandler(AuthService authService, BudgetService budgetService) {
    this.authService = authService;
    this.budgetService = budgetService;
  }

  // Обрабатывает установку нового бюджета для категории
  public void handleSetBudget() {
    try {
      User user = authService.getCurrentUser();
      String category = readString("Введите категорию бюджета: ");
      double amount = readDouble("Введите бюджет: ");

      budgetService.setBudget(user, category, amount);

      printSuccessNotification("Бюджет успешно установлен!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при добавлении бюджета: " + error.getMessage());
    }
  }

  // Отображает текущие бюджеты пользователя
  public void handleWatchBudget() {
    try {
      User user = authService.getCurrentUser();
      System.out.println(user.getLogin() + ", ваш текущий бюджет:");
      Wallet wallet = user.getWallet();
      if (wallet.getBudgets().isEmpty()) {
        System.out.println("Информация по бюджету отсутствует.");
      } else {
        user.getWallet().getBudgetsList().forEach(System.out::println);
      }
    } catch (IllegalArgumentException error) {
      printErrorNotification("Ошибка при получении информации по бюджету: " + error.getMessage());
    }
  }

  // Основной метод обновления бюджета
  public void handleUpdateBudget() {
    try {
      handleWatchBudget();
      showUpdateBudgetMenu();
      handleUpdateBudgetMenu();
    } catch (IllegalArgumentException error) {
      printErrorNotification("Ошибка: " + error.getMessage());
    }
  }

  // Отображает меню редактирования бюджета
  private void showUpdateBudgetMenu() {
    System.out.println("1 - Отредактировать лимит");
    System.out.println("2 - Отредактировать категорию");
    System.out.println("help - Справка");
  }

  // Обрабатывает выбор опции в меню редактирования бюджета
  private void handleUpdateBudgetMenu() {
    String choice = readString("Выберите нужную опцию: ");

    switch (choice) {
      case "1":
        handleUpdateBudgetLimit();
        break;
      case "2":
        handleUpdateBudgetCategory();
        break;
      case "help":
        showHelp();
        break;
      default:
        printErrorNotification("Некорректная опция!");
    }
  }

  // Обрабатывает обновление лимита бюджета для существующей категории
  private void handleUpdateBudgetLimit() {
    try {
      User user = authService.getCurrentUser();
      String category = readString("Введите категорию бюджета: ");
      double limit = readDouble("Введите новый лимит: ");
      budgetService.updateBudget(user, category, limit);

      printSuccessNotification("Лимит категории " + category + " успешно обновлен!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при обновлении бюджета: " + error.getMessage());
    }
  }

  // Обрабатывает изменение названия категории бюджета
  private void handleUpdateBudgetCategory() {
    try {
      User user = authService.getCurrentUser();
      String oldCategory = readString("Введите текущее название категории: ");
      String newCategory = readString("Введите новое название категории: ");
      budgetService.updateBudget(user, oldCategory, newCategory);

      printSuccessNotification(
          "Название категории " + oldCategory + " успешно изменено на " + newCategory + "!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при обновлении бюджета: " + error.getMessage());
    }
  }
}
