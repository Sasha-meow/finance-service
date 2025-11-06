package oop.finance.presentation;

import oop.finance.exception.InvalidCredentialsException;
import oop.finance.exception.UserNotFoundException;
import oop.finance.model.User;
import oop.finance.presentation.base.BaseUI;
import oop.finance.service.AuthService;
import oop.finance.service.FinanceService;

/** Класс-обработчик пользовательского интерфейса для работы с транзакциями. */
public class TransactionHandler extends BaseUI {
  private final AuthService authService;
  private final FinanceService financeService;

  public TransactionHandler(AuthService authService, FinanceService financeService) {
    this.authService = authService;
    this.financeService = financeService;
  }

  // Обрабатывает добавление дохода
  public void handleAddIncome() {
    try {
      String category = readString("Введите категорию дохода: ");
      double amount = readDouble("Введите сумму дохода: ");

      financeService.addIncome(authService.getCurrentUser(), category, amount);
      printSuccessNotification("Доход успешно добавлен!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при добавлении дохода: " + error.getMessage());
    }
  }

  // Обрабатывает добавление расхода
  public void handleAddExpense() {
    try {
      User user = authService.getCurrentUser();
      String category = readString("Введите категорию расхода: ");
      double amount = readDouble("Введите сумму расхода: ");

      financeService.addExpense(user, category, amount);

      printSuccessNotification("Расход успешно добавлен!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при добавлении расхода: " + error.getMessage());
    }
  }

  // Обрабатывает операцию перевода
  public void handleMakeTransfer() {
    try {
      User userFrom = authService.getCurrentUser();
      String login = readString("Введите логин пользователя, которому хотите перевести: ");
      User userTo = authService.getUserByLogin(login);
      double amount = readDouble("Введите сумму перевода: ");

      financeService.makeTransfer(userFrom, userTo, amount);

      printSuccessNotification("Перевод успешно выполнен!");
    } catch (IllegalArgumentException | UserNotFoundException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при переводе: " + error.getMessage());
    }
  }
}
