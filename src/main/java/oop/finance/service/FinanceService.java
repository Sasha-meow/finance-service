package oop.finance.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import oop.finance.exception.InvalidCredentialsException;
import oop.finance.model.StatisticsQuery;
import oop.finance.model.Transaction;
import oop.finance.model.User;
import oop.finance.service.base.BaseService;
import oop.finance.utils.DateTimeUtils;

/**
 * Сервис для управления финансовыми операциями и статистикой. Предоставляет методы для работы с
 * транзакциями и статистическими данными
 */
public class FinanceService extends BaseService {
  // Добавляет операцию дохода для пользователя
  public void addIncome(User user, String category, double amount) {
    validateNotNull(category, "Поле категория");
    validatePositive(amount, "Поле сумма");

    Transaction transaction = new Transaction(amount, category, true);
    user.getWallet().addTransaction(transaction);
  }

  // Добавляет операцию расхода для пользователя
  public void addExpense(User user, String category, double amount) {
    validateNotNull(category, "Поле категория");
    validatePositive(amount, "Поле сумма");

    Transaction transaction = new Transaction(amount, category, false);
    user.getWallet().addTransaction(transaction);
  }

  // Выполняет перевод средств между пользователями
  public void makeTransfer(User userFrom, User userTo, double amount) {
    validatePositive(amount, "Поле сумма перевода");

    if (userFrom.equals(userTo)) {
      throw new InvalidCredentialsException("Нельзя перевести самому себе!");
    }

    Transaction transactionFrom = new Transaction(amount, "Перевод " + userTo.getLogin(), false);
    userFrom.getWallet().addTransaction(transactionFrom);

    Transaction transactionTo = new Transaction(amount, "Перевод от " + userFrom.getLogin(), true);
    userTo.getWallet().addTransaction(transactionTo);
  }

  // Универсальный метод для расчета общей суммы транзакций по заданному запросу
  public double calculateTotal(User user, StatisticsQuery query) {
    return user.getWallet().getTransactions().stream()
        .filter(query.build())
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  // Группирует транзакции по категориям согласно заданному запросу
  public Map<String, Double> groupByCategory(User user, StatisticsQuery query) {
    return user.getWallet().getTransactions().stream()
        .filter(query.build())
        .collect(
            Collectors.groupingBy(
                Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
  }

  // Метод для получения общей статистики доходов
  public double getTotalIncome(User user) {
    return calculateTotal(user, StatisticsQuery.create().withType(true));
  }

  // Методы для получения общей статистики доходов по переданным категориям
  public double getTotalIncome(User user, String[] categories) {
    validateNotEmpty(categories, "Значение категории");

    return calculateTotal(user, StatisticsQuery.create().withType(true).withCategories(categories));
  }

  // Методы для получения статистики доходов за период
  public double getTotalIncome(User user, String dateFromStr, String dateToStr) {
    LocalDateTime dateFrom = DateTimeUtils.parseStringDate(dateFromStr);
    LocalDateTime dateTo = DateTimeUtils.parseStringDate(dateToStr);

    return calculateTotal(
        user, StatisticsQuery.create().withType(true).withPeriod(dateFrom, dateTo));
  }

  // Метод для получения общей статистики расходов
  public double getTotalExpense(User user) {
    return calculateTotal(user, StatisticsQuery.create().withType(false));
  }

  // Методы для получения общей статистики расходов по переданным категориям
  public double getTotalExpense(User user, String[] categories) {
    validateNotEmpty(categories, "Значение категории");

    return calculateTotal(
        user, StatisticsQuery.create().withType(false).withCategories(categories));
  }

  // Методы для получения статистики расходов за период
  public double getTotalExpense(User user, String dateFromStr, String dateToStr) {
    LocalDateTime dateFrom = DateTimeUtils.parseStringDate(dateFromStr);
    LocalDateTime dateTo = DateTimeUtils.parseStringDate(dateToStr);

    return calculateTotal(
        user, StatisticsQuery.create().withType(false).withPeriod(dateFrom, dateTo));
  }

  // Проверяет, превышают ли расходы доходы пользователя
  public boolean isTotalExceeding(User user) {
    return getTotalExpense(user) > getTotalIncome(user);
  }

  // Методы для получения доходных транзакций по категориям
  public Map<String, Double> getIncomeByCategory(User user) {
    return groupByCategory(user, StatisticsQuery.create().withType(true));
  }

  // Методы для получения доходных транзакций по выбранным категориям
  public Map<String, Double> getIncomeByCategory(User user, String[] categories) {
    validateNotEmpty(categories, "Значение категории");

    return groupByCategory(
        user, StatisticsQuery.create().withType(true).withCategories(categories));
  }

  // Методы для получения доходных транзакций по периоду
  public Map<String, Double> getIncomeByCategory(User user, String dateFromStr, String dateToStr) {
    LocalDateTime dateFrom = DateTimeUtils.parseStringDate(dateFromStr);
    LocalDateTime dateTo = DateTimeUtils.parseStringDate(dateToStr);
    return groupByCategory(
        user, StatisticsQuery.create().withType(true).withPeriod(dateFrom, dateTo));
  }

  // Методы для получения расходных транзакций по категориям
  public Map<String, Double> getExpensesByCategory(User user) {
    return groupByCategory(user, StatisticsQuery.create().withType(false));
  }

  // Методы для получения расходных транзакций по выбранным категориям
  public Map<String, Double> getExpensesByCategory(User user, String[] categories) {
    validateNotEmpty(categories, "Значение категории");

    return groupByCategory(
        user, StatisticsQuery.create().withType(false).withCategories(categories));
  }

  // Методы для получения расходных транзакций по периоду
  public Map<String, Double> getExpensesByCategory(
      User user, String dateFromStr, String dateToStr) {
    LocalDateTime dateFrom = DateTimeUtils.parseStringDate(dateFromStr);
    LocalDateTime dateTo = DateTimeUtils.parseStringDate(dateToStr);

    if (dateTo.isBefore(dateFrom)) {
      throw new InvalidCredentialsException("Конечная дата не может быть раньше начальной!");
    }

    return groupByCategory(
        user, StatisticsQuery.create().withType(false).withPeriod(dateFrom, dateTo));
  }
}
