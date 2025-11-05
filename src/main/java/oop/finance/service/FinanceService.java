package oop.finance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import oop.finance.constants.BaseConstants;
import oop.finance.exception.InvalidCredentialsException;
import oop.finance.model.Budget;
import oop.finance.model.Transaction;
import oop.finance.model.User;
import oop.finance.service.base.BaseService;

public class FinanceService extends BaseService {
  public void addIncome(User user, String category, double amount) {
    validateNotNull(category, "Поле категория");
    validatePositive(amount, "Поле сумма");

    Transaction transaction = new Transaction(amount, category, true);
    user.getWallet().addTransaction(transaction);
  }

  public void addExpense(User user, String category, double amount) {
    validateNotNull(category, "Поле категория");
    validatePositive(amount, "Поле сумма");

    Transaction transaction = new Transaction(amount, category, false);
    user.getWallet().addTransaction(transaction);
  }

  public void makeTransfer(User userFrom, User userTo, double amount) {
    validatePositive(amount, "Поле сумма");

    if (userFrom.equals(userTo)) {
      throw new InvalidCredentialsException("Нельзя перевести самому себе!");
    }

    Transaction transactionFrom = new Transaction(amount, "Перевод " + userTo.getLogin(), false);
    userFrom.getWallet().addTransaction(transactionFrom);

    Transaction transactionTo = new Transaction(amount, "Перевод от" + userFrom.getLogin(), true);
    userTo.getWallet().addTransaction(transactionTo);
  }

  public void setBudget(User user, String category, double amount) {
    validateNotNull(category, "Поле категория");
    validatePositive(amount, "Поле сумма");

    Budget budget = new Budget(category, amount);
    double sent = getExpensesSumByCategory(user, category);
    if (sent > 0) {
      budget.addExpense(sent);
    }
    user.getWallet().setBudget(budget);
  }

  public double getTotalIncome(User user) {
    return user.getWallet().getTransactions().stream()
        .filter(Transaction::isIncome)
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  public double getTotalIncome(User user, String[] categories) {
    validateNotEmpty(categories, "Значение категории");

    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                transaction.isIncome()
                    && Arrays.asList(categories).contains(transaction.getCategory()))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  public double getTotalIncome(User user, String dateFromStr, String dateToStr) {
    LocalDateTime dateFrom = parseStringDate(dateFromStr);
    LocalDateTime dateTo = parseStringDate(dateToStr);
    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                transaction.isIncome()
                    && transaction.getCreatedAt().isBefore(dateTo)
                    && transaction.getCreatedAt().isAfter(dateFrom))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  public double getTotalExpense(User user) {
    return user.getWallet().getTransactions().stream()
        .filter(transaction -> !transaction.isIncome())
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  public double getTotalExpense(User user, String[] categories) {
    validateNotEmpty(categories, "Значение категории");

    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                !transaction.isIncome()
                    && Arrays.asList(categories).contains(transaction.getCategory()))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  public double getTotalExpense(User user, String dateFromStr, String dateToStr) {
    LocalDateTime dateFrom = parseStringDate(dateFromStr);
    LocalDateTime dateTo = parseStringDate(dateToStr);
    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                !transaction.isIncome()
                    && transaction.getCreatedAt().isBefore(dateTo)
                    && transaction.getCreatedAt().isAfter(dateFrom))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  public boolean isTotalExceeding(User user) {
    return getTotalExpense(user) > getTotalIncome(user);
  }

  private double getExpensesSumByCategory(User user, String category) {
    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                !transaction.isIncome() && Objects.equals(transaction.getCategory(), category))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  public Map<String, Double> getIncomeByCategory(User user) {
    return user.getWallet().getTransactions().stream()
        .filter(Transaction::isIncome)
        .collect(
            Collectors.groupingBy(
                Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
  }

  public Map<String, Double> getIncomeByCategory(User user, String[] categories) {
    validateNotEmpty(categories, "Значение категории");

    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                transaction.isIncome()
                    && Arrays.asList(categories).contains(transaction.getCategory()))
        .collect(
            Collectors.groupingBy(
                Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
  }

  public Map<String, Double> getIncomeByCategory(User user, String dateFromStr, String dateToStr) {
    LocalDateTime dateFrom = parseStringDate(dateFromStr);
    LocalDateTime dateTo = parseStringDate(dateToStr);
    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                transaction.isIncome()
                    && transaction.getCreatedAt().isBefore(dateTo)
                    && transaction.getCreatedAt().isAfter(dateFrom))
        .collect(
            Collectors.groupingBy(
                Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
  }

  public Map<String, Double> getExpensesByCategory(User user) {
    return user.getWallet().getTransactions().stream()
        .filter(transaction -> !transaction.isIncome())
        .collect(
            Collectors.groupingBy(
                Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
  }

  public Map<String, Double> getExpensesByCategory(User user, String[] categories) {
    validateNotEmpty(categories, "Значение категории");

    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                !transaction.isIncome()
                    && Arrays.asList(categories).contains(transaction.getCategory()))
        .collect(
            Collectors.groupingBy(
                Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
  }

  public Map<String, Double> getExpensesByCategory(
      User user, String dateFromStr, String dateToStr) {
    LocalDateTime dateFrom = parseStringDate(dateFromStr);
    LocalDateTime dateTo = parseStringDate(dateToStr);

    if (dateTo.isBefore(dateFrom)) {
      throw new InvalidCredentialsException("Конечная дата не может быть раньше начальной!");
    }

    return user.getWallet().getTransactions().stream()
        .filter(
            transaction ->
                !transaction.isIncome()
                    && transaction.getCreatedAt().isBefore(dateTo)
                    && transaction.getCreatedAt().isAfter(dateFrom))
        .collect(
            Collectors.groupingBy(
                Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
  }

  public LocalDateTime parseStringDate(String str) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BaseConstants.DATE_FORMAT);
      LocalDate date = LocalDate.parse(str, formatter);
      return date.atStartOfDay();
    } catch (DateTimeParseException e) {
      throw new InvalidCredentialsException("Неверный формат даты. Используйте дд.мм.гггг");
    }
  }

  public double getBudgetRemaining(User user, String category) {
    validateNotNull(category, "Поле категория");

    Budget budget = user.getWallet().getBudget(category);

    return budget != null ? budget.getRemaining() : 0.0;
  }

  public void updateBudget(User user, String category, double limit) {
    validateNotNull(category, "Поле категория");
    validatePositive(limit, "Поле лимит");
    Budget budget = user.getWallet().getBudget(category);

    if (budget == null) {
      throw new InvalidCredentialsException("Бюджет по заданной категории не найден!");
    }

    budget.setLimit(limit);
  }

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
