package oop.finance.presentation;

import java.time.format.DateTimeParseException;
import java.util.Map;
import oop.finance.exception.InvalidCredentialsException;
import oop.finance.model.Budget;
import oop.finance.model.User;
import oop.finance.presentation.base.BaseUI;
import oop.finance.service.AuthService;
import oop.finance.service.BudgetService;
import oop.finance.service.FinanceService;

/** Класс-обработчик пользовательского интерфейса для работы со статистикой. */
public class StatisticsHandler extends BaseUI {
  private final AuthService authService;
  private final FinanceService financeService;
  private final BudgetService budgetService;

  public StatisticsHandler(
      AuthService authService, FinanceService financeService, BudgetService budgetService) {
    this.authService = authService;
    this.financeService = financeService;
    this.budgetService = budgetService;
  }

  // Отображает и обрабатывает меню статистики
  public void showStatisticsMenu() {
    statisticsMenu();
    handleStatisticsMenu();
  }

  // Отображает меню статистики
  private void statisticsMenu() {
    System.out.println("1 - Просмотреть общую статистику");
    System.out.println("2 - Просмотреть статистику по категориям");
    System.out.println("3 - Просмотреть статистику по периоду");
    System.out.println("help - Справка");
  }

  // Обрабатывает выбор из меню статистики
  private void handleStatisticsMenu() {
    String choice = readString("Выберите нужную опцию: ");

    switch (choice) {
      case "1":
        showStatistics();
        break;
      case "2":
        showStatisticsByCategory();
        break;
      case "3":
        showStatisticsByPeriod();
        break;
      case "help":
        showHelp();
        break;
      default:
        printErrorNotification("Некорректная опция!");
    }
  }

  // Отображает общую сводную статистику по всем операциям (доходы/расходы + детализация по
  // категориям)
  private void showStatistics() {
    try {
      User user = authService.getCurrentUser();
      double totalIncome = financeService.getTotalIncome(user);
      double totalExpenses = financeService.getTotalExpense(user);

      System.out.println("Общий доход: " + totalIncome);
      System.out.println("Общий расход: " + totalExpenses);

      // Доходы по категориям
      Map<String, Double> incomeByCategory = financeService.getIncomeByCategory(user);
      if (!incomeByCategory.isEmpty()) {
        System.out.println("Доходы по категориям:");
        incomeByCategory.forEach(
            (category, amount) -> System.out.println(" " + category + ": " + amount));
      } else {
        System.out.println("Доходы по категориям отсутствуют.");
      }

      // Расходы по категориям с бюджетами
      Map<String, Double> expensesByCategory = financeService.getExpensesByCategory(user);
      if (!expensesByCategory.isEmpty()) {
        System.out.println("Расходы по категориям:");
        expensesByCategory.forEach(
            (category, amount) -> {
              System.out.println(" " + category + ": " + amount);
              Budget budget = user.getWallet().getBudget(category);
              if (budget != null) {
                double remaining = budgetService.getBudgetRemaining(user, category);
                System.out.println(
                    "  Бюджет: " + budget.getLimit() + ", Оставшийся бюджет: " + remaining);
              }
            });
      } else {
        System.out.println("Расходы по категориям отсутствуют.");
      }

      // Проверка превышения расходов над доходами
      if (financeService.isTotalExceeding(user)) {
        printInfoNotification("Внимание: Ваши расходы превышают доходы!");
      }
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при сборе статистики: " + error.getMessage());
    }
  }

  // Отображает статистику по выбранным пользователем категориям
  private void showStatisticsByCategory() {
    try {
      String str =
          readString("Введите категории, по которым хотите просмотреть статистику через |:\n");
      String[] categories = str.split("\\|");
      User user = authService.getCurrentUser();
      double totalIncome = financeService.getTotalIncome(user, categories);
      double totalExpenses = financeService.getTotalExpense(user, categories);

      System.out.println("Общий доход по выбранным категориям: " + totalIncome);
      System.out.println("Общий расход по выбранным категориям: " + totalExpenses);

      // Доходы по категориям
      Map<String, Double> incomeByCategory = financeService.getIncomeByCategory(user, categories);
      if (!incomeByCategory.isEmpty()) {
        System.out.println("Доходы по выбранным категориям:");
        incomeByCategory.forEach(
            (category, amount) -> System.out.println(" " + category + ": " + amount));
      } else {
        System.out.println("Доходы по выбранным категориям отсутствуют.");
      }

      // Расходы по категориям с бюджетами
      Map<String, Double> expensesByCategory =
          financeService.getExpensesByCategory(user, categories);
      if (!expensesByCategory.isEmpty()) {
        System.out.println("Расходы по выбранным категориям:");
        expensesByCategory.forEach(
            (category, amount) -> {
              System.out.println(" " + category + ": " + amount);
              Budget budget = user.getWallet().getBudget(category);
              if (budget != null) {
                double remaining = budgetService.getBudgetRemaining(user, category);
                System.out.println(
                    "  Бюджет: " + budget.getLimit() + ", Оставшийся бюджет: " + remaining);
              }
            });
      } else {
        System.out.println("Расходы по выбранным категориям отсутствуют.");
      }
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при сборе статистики: " + error.getMessage());
    }
  }

  // Отображает статистику за указанный временной период
  private void showStatisticsByPeriod() {
    try {
      System.out.println("Введите период времени в формате дд.мм.гггг:");
      String dateFrom = readString("Начальная дата: ");
      String dateTo = readString("Конечная дата: ");

      User user = authService.getCurrentUser();
      double totalIncome = financeService.getTotalIncome(user, dateFrom, dateTo);
      double totalExpenses = financeService.getTotalExpense(user, dateFrom, dateTo);

      System.out.println("Общий доход за период " + dateFrom + " - " + dateTo + ": " + totalIncome);
      System.out.println(
          "Общий расход за период " + dateFrom + " - " + dateTo + ": " + totalExpenses);

      Map<String, Double> incomeByCategory =
          financeService.getIncomeByCategory(user, dateFrom, dateTo);
      if (!incomeByCategory.isEmpty()) {
        System.out.println("Доходы за период " + dateFrom + " - " + dateTo + ":");
        incomeByCategory.forEach(
            (category, amount) -> System.out.println(" " + category + ": " + amount));
      } else {
        System.out.println("Доходы за заданный период отсутствуют.");
      }

      Map<String, Double> expensesByCategory =
          financeService.getExpensesByCategory(user, dateFrom, dateTo);
      if (!expensesByCategory.isEmpty()) {
        System.out.println("Расходы за период " + dateFrom + " - " + dateTo + ":");
        expensesByCategory.forEach(
            (category, amount) -> System.out.println(" " + category + ": " + amount));
      } else {
        System.out.println("Расходы за заданный период отсутствуют.");
      }
    } catch (IllegalArgumentException
        | InvalidCredentialsException
        | DateTimeParseException error) {
      printErrorNotification("Ошибка при сборе статистики: " + error.getMessage());
    }
  }
}
