package oop.finance.service;

import java.util.ArrayList;
import java.util.List;
import oop.finance.constants.BaseConstants;
import oop.finance.model.Budget;
import oop.finance.model.User;

/** Сервис уведомлений для мониторинга финансового состояния */
public class NotificationService {
  private final FinanceService financeService;

  public NotificationService(FinanceService financeService) {
    this.financeService = financeService;
  }

  // Проверяет все возможные уведомления для пользователя
  public List<String> checkNotifications(User user) {
    List<String> notifications = new ArrayList<>();

    notifications.addAll(checkBudgetNotifications(user));
    notifications.addAll(checkBalanceNotifications(user));
    notifications.addAll(checkFinancialHealthNotifications(user));

    return notifications;
  }

  // Проверяет уведомления, связанные с бюджетами
  private List<String> checkBudgetNotifications(User user) {
    List<String> notifications = new ArrayList<>();

    for (Budget budget : user.getWallet().getBudgetsList()) {
      if (budget != null) {
        double usagePercentage = budget.getUsagePercentage();

        // Уведомление о 80% лимита
        if (usagePercentage >= BaseConstants.MEDIUM_PERCENT
            && usagePercentage < BaseConstants.MAX_PERCENT) {
          notifications.add(
              String.format(
                  "Бюджет категории %s использован на %.1f%% (осталось: %.2f)",
                  budget.getCategory(), usagePercentage, budget.getRemaining()));
        }

        // Уведомление о превышении лимита
        if (budget.isExceeded()) {
          notifications.add(
              String.format(
                  "Превышен бюджет категории %s! Перерасход: %.2f",
                  budget.getCategory(), Math.abs(budget.getRemaining())));
        }
      }
    }

    return notifications;
  }

  // Проверяет уведомления, связанные с балансом кошелька
  private List<String> checkBalanceNotifications(User user) {
    List<String> notifications = new ArrayList<>();
    double balance = user.getWallet().getBalance();

    if (balance == 0) {
      notifications.add("Ваш баланс равен нулю");
    }

    if (balance < 0) {
      notifications.add(String.format("Отрицательный баланс: %.2f", balance));
    }

    double totalIncome = financeService.getTotalIncome(user);
    if (totalIncome > 0 && balance < (totalIncome * BaseConstants.MIN_PERCENT)) {
      notifications.add(
          String.format("Низкий баланс: %.2f (менее 10%% от общего дохода)", balance));
    }

    return notifications;
  }

  // Проверяет уведомления об общем финансовом здоровье
  private List<String> checkFinancialHealthNotifications(User user) {
    List<String> notifications = new ArrayList<>();

    if (financeService.isTotalExceeding(user)) {
      double totalIncome = financeService.getTotalIncome(user);
      double totalExpenses = financeService.getTotalExpense(user);
      double difference = totalExpenses - totalIncome;

      notifications.add(String.format("Расходы превышают доходы на %.2f", difference));
    }

    return notifications;
  }
}
