package oop.finance.presentation;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import oop.finance.exception.InvalidCredentialsException;
import oop.finance.exception.UserNotFoundException;
import oop.finance.model.Budget;
import oop.finance.model.User;
import oop.finance.model.Wallet;
import oop.finance.presentation.base.BaseUI;
import oop.finance.service.AuthService;
import oop.finance.service.FinanceService;
import oop.finance.service.NotificationService;
import oop.finance.service.ReportService;

public class ConsoleUI extends BaseUI {
  private final AuthService authService;
  private final FinanceService financeService;
  private final ReportService reportService;
  private final NotificationService notificationService;

  public ConsoleUI(
      AuthService authService,
      FinanceService financeService,
      ReportService reportService,
      NotificationService notificationService) {
    this.authService = authService;
    this.financeService = financeService;
    this.reportService = reportService;
    this.notificationService = notificationService;
  }

  public void start() {
    System.out.println(
        "Привет, незнакомец! Для продолжения работы с приложением необходимо авторизоваться.");

    while (true) {
      if (authService.getCurrentUser() == null) {
        showAuthMenu();
        handleAuthMenu();
      } else {
        showNotifications();
        showMainMenu();
        handleMainMenu();
      }
    }
  }

  private void showAuthMenu() {
    System.out.println("1 - Войти по логину и паролю");
    System.out.println("2 - Зарегистрироваться");
    System.out.println("0 - Завершить работу");
    System.out.println("help - Справка");
  }

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

  private void handleExit() {
    authService.logout();
    System.out.println("До скорых встреч!");
    scanner.close();
    System.exit(0);
  }

  private void showHelp() {
    System.out.println("Справка:");
    System.out.println(
        "Программа для контроля бюджета. Для начала использования - необходимо зарегистрироваться.");
    System.out.println("При завершении работы сохраняет данные в JSON-файл.");
  }

  private void showMainMenu() {
    System.out.println("1 - Установить бюджет");
    System.out.println("2 - Просмотреть установленный бюджет");
    System.out.println("3 - Отредактировать установленный бюджет");
    System.out.println("4 - Добавить доход");
    System.out.println("5 - Добавить расход");
    System.out.println("6 - Выполнить перевод");
    System.out.println("7 - Просмотр статистики");
    System.out.println("8 - Выгрузить отчет");
    System.out.println("9 - Выйти из учетной записи");
    System.out.println("0 - Завершить работу");
    System.out.println("help - Справка");
  }

  private void handleMainMenu() {
    String choice = readString("Выберите нужную опцию: ");

    switch (choice) {
      case "0":
        handleExit();
        break;
      case "1":
        handleSetBudget();
        break;
      case "2":
        handleWatchBudget();
        break;
      case "3":
        handleUpdateBudget();
        break;
      case "4":
        handleAddIncome();
        break;
      case "5":
        handleAddExpense();
        break;
      case "6":
        handleMakeTransfer();
        break;
      case "7":
        showStatisticsMenu();
        handleStatisticsMenu();
        break;
      case "8":
        handleExportReport();
        break;
      case "9":
        authService.logout();
        break;
      case "help":
        showHelp();
        break;
      default:
        printErrorNotification("Некорректная опция!");
    }
  }

  private void handleAddIncome() {
    try {
      String category = readString("Введите категорию дохода: ");
      double amount = readDouble("Введите сумму дохода: ");

      financeService.addIncome(authService.getCurrentUser(), category, amount);
      printSuccessNotification("Доход успешно добавлен!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при добавлении дохода: " + error.getMessage());
    }
  }

  private void handleAddExpense() {
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

  private void handleMakeTransfer() {
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

  private void handleSetBudget() {
    try {
      User user = authService.getCurrentUser();
      String category = readString("Введите категорию бюджета: ");
      double amount = readDouble("Введите бюджет: ");

      financeService.setBudget(user, category, amount);

      printSuccessNotification("Бюджет успешно установлен!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при добавлении бюджета: " + error.getMessage());
    }
  }

  private void handleWatchBudget() {
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

  private void handleUpdateBudget() {
    try {
      handleWatchBudget();
      showUpdateBudgetMenu();
      handleUpdateBudgetMenu();
    } catch (IllegalArgumentException error) {
      printErrorNotification("Ошибка: " + error.getMessage());
    }
  }

  private void showUpdateBudgetMenu() {
    System.out.println("1 - Отредактировать лимит");
    System.out.println("2 - Отредактировать категорию");
    System.out.println("help - Справка");
  }

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

  private void handleUpdateBudgetLimit() {
    try {
      User user = authService.getCurrentUser();
      String category = readString("Введите категорию бюджета: ");
      double limit = readDouble("Введите новый лимит: ");
      financeService.updateBudget(user, category, limit);

      printSuccessNotification("Лимит категории " + category + " успешно обновлен!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при обновлении бюджета: " + error.getMessage());
    }
  }

  private void handleUpdateBudgetCategory() {
    try {
      User user = authService.getCurrentUser();
      String oldCategory = readString("Введите текущее название категории: ");
      String newCategory = readString("Введите новое название категории: ");
      financeService.updateBudget(user, oldCategory, newCategory);

      printSuccessNotification(
          "Название категории " + oldCategory + " успешно изменено на " + newCategory + "!");
    } catch (IllegalArgumentException | InvalidCredentialsException error) {
      printErrorNotification("Ошибка при обновлении бюджета: " + error.getMessage());
    }
  }

  private void showStatisticsMenu() {
    System.out.println("1 - Просмотреть общую статистику");
    System.out.println("2 - Просмотреть статистику по категориям");
    System.out.println("3 - Просмотреть статистику по периоду");
    System.out.println("help - Справка");
  }

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
                double remaining = financeService.getBudgetRemaining(user, category);
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
                double remaining = financeService.getBudgetRemaining(user, category);
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

  private void handleExportReport() {
    try {
      String path =
          readStringWithEmptiness(
              "Введите путь для сохранения файла (или нажмите Enter для сохранения в текущей папке): ");
      String resultPath;

      User user = authService.getCurrentUser();
      if (path.isEmpty()) {
        resultPath = reportService.generateReport(user);
      } else {
        resultPath = reportService.generateReport(user, path + ".csv");
      }

      printSuccessNotification("Отчёт " + resultPath + " успешно выгружен!");
    } catch (Exception error) {
      printErrorNotification("Ошибка при выгрузке отчета: " + error.getMessage());
    }
  }

  private void showNotifications() {
    if (authService.getCurrentUser() != null) {
      List<String> notifications =
          notificationService.checkNotifications(authService.getCurrentUser());

      if (!notifications.isEmpty()) {
        for (String notification : notifications) {
          printInfoNotification(notification);
        }
      }
    }
  }
}
