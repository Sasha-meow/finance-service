package oop.finance;

import java.io.IOException;
import oop.finance.presentation.AuthHandler;
import oop.finance.presentation.BudgetHandler;
import oop.finance.presentation.ConsoleUI;
import oop.finance.presentation.ReportsHandler;
import oop.finance.presentation.StatisticsHandler;
import oop.finance.presentation.TransactionHandler;
import oop.finance.repository.UserRepository;
import oop.finance.service.AuthService;
import oop.finance.service.BudgetService;
import oop.finance.service.FinanceService;
import oop.finance.service.NotificationService;
import oop.finance.service.ReportService;
import oop.finance.utils.ConfigReader;

public class Main {
  private static final ConfigReader CONFIG_READER = new ConfigReader();

  public static void main(String[] args) {
    loadConfig();
    String userRepoPath = CONFIG_READER.getProperties().getProperty("users.src");
    UserRepository userRepository = new UserRepository(userRepoPath);
    AuthService authService = new AuthService(userRepository);
    FinanceService financeService = new FinanceService();
    BudgetService budgetService = new BudgetService();
    NotificationService notificationService = new NotificationService(financeService);
    ReportService reportService = new ReportService();

    AuthHandler authHandler = new AuthHandler(authService);
    TransactionHandler transactionHandler = new TransactionHandler(authService, financeService);
    BudgetHandler budgetHandler = new BudgetHandler(authService, budgetService);
    StatisticsHandler statisticsHandler =
        new StatisticsHandler(authService, financeService, budgetService);
    ReportsHandler reportsHandler = new ReportsHandler(authService, reportService);

    ConsoleUI ui =
        new ConsoleUI(
            authService,
            notificationService,
            authHandler,
            transactionHandler,
            budgetHandler,
            statisticsHandler,
            reportsHandler);

    ui.start();
  }

  public static void loadConfig() {
    try {
      CONFIG_READER.load();
    } catch (IOException e) {
      System.out.println("Ошибка чтения файла конфигурации!");
    }
  }
}
