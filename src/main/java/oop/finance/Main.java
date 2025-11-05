package oop.finance;

import java.io.IOException;
import oop.finance.presentation.ConsoleUI;
import oop.finance.repository.UserRepository;
import oop.finance.service.AuthService;
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
    NotificationService notificationService = new NotificationService(financeService);
    ReportService reportService = new ReportService();

    ConsoleUI ui = new ConsoleUI(authService, financeService, reportService, notificationService);
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
