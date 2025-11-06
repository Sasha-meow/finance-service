package oop.finance.model;

import oop.finance.model.base.BaseEntity;

/** Модель транзакции */
public class Transaction extends BaseEntity {
  private String category;
  private double amount;
  private boolean isIncome;

  public Transaction(double amount, String category, boolean isIncome) {
    this.amount = amount;
    this.category = category;
    this.isIncome = isIncome;
  }

  // Геттеры и сеттеры
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public boolean isIncome() {
    return isIncome;
  }

  public void setIncome(boolean income) {
    isIncome = income;
  }
}
