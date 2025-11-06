package oop.finance.service;

import java.util.Objects;
import oop.finance.exception.InvalidCredentialsException;
import oop.finance.model.Budget;
import oop.finance.model.Transaction;
import oop.finance.model.User;
import oop.finance.service.base.BaseService;

/**
 * Сервис для управления бюджетом. Предоставляет методы для установления и редактирования бюджета
 */
public class BudgetService extends BaseService {
  // Устанавливает новый бюджет c указанной категорией и лимитом
  public void setBudget(User user, String category, double limit) {
    validateNotNull(category, "Поле категория");
    validatePositive(limit, "Поле лимит");

    Budget budget = new Budget(category, limit);
    double sent = getExpensesSumByCategory(user, category);
    if (sent > 0) {
      budget.addExpense(sent);
    }
    user.getWallet().setBudget(budget);
  }

  // Рассчитывает сумму расходов по указанной категории
  private double getExpensesSumByCategory(User user, String category) {
    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                !transaction.isIncome() && Objects.equals(transaction.getCategory(), category))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  // Возвращает остаток бюджета для указанной категории
  public double getBudgetRemaining(User user, String category) {
    validateNotNull(category, "Поле категория");

    Budget budget = user.getWallet().getBudget(category);

    return budget != null ? budget.getRemaining() : 0.0;
  }

  // Обновляет лимит бюджета для существующей категории
  public void updateBudget(User user, String category, double limit) {
    validateNotNull(category, "Поле категория");
    validatePositive(limit, "Поле лимит");
    Budget budget = user.getWallet().getBudget(category);

    if (budget == null) {
      throw new InvalidCredentialsException("Бюджет по заданной категории не найден!");
    }

    budget.setLimit(limit);
  }

  // Изменяет название категории бюджета
  public void updateBudget(User user, String category, String newCategory) {
    validateNotNull(category, "Поле категория");
    validateNotNull(newCategory, "Поле новая категория");

    Budget budget = user.getWallet().getBudget(category);

    if (budget == null) {
      throw new InvalidCredentialsException("Бюджет по заданной категории не найден!");
    }

    Budget newBudget = new Budget(newCategory, budget.getLimit(), budget.getSpent());
    user.getWallet().setBudget(newBudget, category);
  }
}
