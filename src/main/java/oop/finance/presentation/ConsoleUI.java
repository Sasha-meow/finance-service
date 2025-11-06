package oop.finance.presentation;

import java.util.List;
import oop.finance.presentation.base.BaseUI;
import oop.finance.service.AuthService;
import oop.finance.service.NotificationService;

/** Основной класс для работы со слоем презентации. */
public class ConsoleUI extends BaseUI {
  private final AuthService authService;
  private final NotificationService notificationService;
  private final AuthHandler authHandler;
  private final TransactionHandler transactionHandler;
  private final BudgetHandler budgetHandler;
  private final StatisticsHandler statisticsHandler;
  private final ReportsHandler reportsHandler;

  public ConsoleUI(
      AuthService authService,
      NotificationService notificationService,
      AuthHandler authHandler,
      TransactionHandler transactionHandler,
      BudgetHandler budgetHandler,
      StatisticsHandler statisticsHandler,
      ReportsHandler reportsHandler) {
    this.authService = authService;
    this.notificationService = notificationService;
    this.authHandler = authHandler;
    this.transactionHandler = transactionHandler;
    this.budgetHandler = budgetHandler;
    this.statisticsHandler = statisticsHandler;
    this.reportsHandler = reportsHandler;
  }

  public void start() {
    System.out.println(
        "Привет, незнакомец! Для продолжения работы с приложением необходимо авторизоваться.");

    while (true) {
      if (authService.getCurrentUser() == null) {
        authHandler.showAndHandleAuthMenu();
      } else {
        showNotifications();
        showMainMenu();
        handleMainMenu();
      }
    }
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

  private void handleExit() {
    authService.logout();
    System.out.println("До скорых встреч!");
    scanner.close();
    System.exit(0);
  }

  private void handleMainMenu() {
    String choice = readString("Выберите нужную опцию: ");

    switch (choice) {
      case "0":
        handleExit();
        break;
      case "1":
        budgetHandler.handleSetBudget();
        break;
      case "2":
        budgetHandler.handleWatchBudget();
        break;
      case "3":
        budgetHandler.handleUpdateBudget();
        break;
      case "4":
        transactionHandler.handleAddIncome();
        break;
      case "5":
        transactionHandler.handleAddExpense();
        break;
      case "6":
        transactionHandler.handleMakeTransfer();
        break;
      case "7":
        statisticsHandler.showStatisticsMenu();
        break;
      case "8":
        reportsHandler.handleExportReport();
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
