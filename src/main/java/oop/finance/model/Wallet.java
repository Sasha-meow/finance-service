package oop.finance.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oop.finance.model.base.BaseEntity;

public class Wallet extends BaseEntity {
  private double balance;
  private List<Transaction> transactions;
  private Map<String, Budget> budgets;

  public Wallet(String userId) {
    this.balance = 0;
    this.transactions = new ArrayList<>();
    this.budgets = new HashMap<>();
    this.id = userId;
  }

  public Wallet(double balance, List<Transaction> transactions, Map<String, Budget> budgets) {
    this.balance = balance;
    this.transactions = transactions;
    this.budgets = budgets;
  }

  public void addTransaction(Transaction transaction) {
    transactions.add(transaction);

    if (transaction.isIncome()) {
      balance += transaction.getAmount();
    } else {
      balance -= transaction.getAmount();
      updateBudget(transaction);
    }
  }

  private void updateBudget(Transaction transaction) {
    Budget budget = budgets.get(transaction.getCategory());
    if (budget != null) {
      budget.addExpense(transaction.getAmount());
    }
  }

  // Геттеры и сеттеры
  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public List<Transaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
  }

  public Map<String, Budget> getBudgets() {
    return budgets;
  }

  public List<Budget> getBudgetsList() {
    return new ArrayList<>(budgets.values());
  }

  public void setBudgets(Map<String, Budget> budgets) {
    this.budgets = budgets;
  }

  public void setBudget(Budget budget) {
    budgets.put(budget.getCategory(), budget);
  }

  public void setBudget(Budget budget, String oldCategory) {
    budgets.remove(oldCategory);
    setBudget(budget);
  }

  public Budget getBudget(String category) {
    return budgets.get(category);
  }
}
