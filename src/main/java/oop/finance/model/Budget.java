package oop.finance.model;

import oop.finance.constants.BaseConstants;
import oop.finance.exception.InvalidCredentialsException;
import oop.finance.model.base.BaseEntity;

public class Budget extends BaseEntity {
  private String category;
  private double limit;
  private double spent;

  public Budget(String category, double limit) {
    validateLimit(limit);

    this.category = category;
    this.limit = limit;
    this.spent = 0;
  }

  public Budget(String category, double limit, double spent) {
    validateLimit(limit);

    this.category = category;
    this.limit = limit;
    this.spent = spent;
  }

  public void validateLimit(double newLimit) {
    if (newLimit < 0) {
      throw new InvalidCredentialsException("Лимит не может быть отрицательным!");
    }
  }

  // Добавление трат
  public void addExpense(double amount) {
    spent += amount;
  }

  // Превышен ли лимит
  public boolean isExceeded() {
    return spent > limit;
  }

  // Получение остатка
  public double getRemaining() {
    return limit - spent;
  }

  public double getUsagePercentage() {
    return limit > 0 ? (spent / limit) * BaseConstants.MAX_PERCENT : 0;
  }

  // Геттеры и сеттеры
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public double getLimit() {
    return limit;
  }

  public void setLimit(double limit) {
    validateLimit(limit);

    this.limit = limit;
  }

  public double getSpent() {
    return spent;
  }

  @Override
  public String toString() {
    return category + ": " + limit + " (потрачено: " + spent + ")";
  }
}
