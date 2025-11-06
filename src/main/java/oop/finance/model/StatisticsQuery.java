package oop.finance.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Построитель запросов для фильтрации транзакций при формировании статистики. Реализует паттерн
 * Builder для создания сложных предикатов
 */
public class StatisticsQuery {
  private final List<Predicate<Transaction>> filters = new ArrayList<>();

  public static StatisticsQuery create() {
    return new StatisticsQuery();
  }

  // Добавляет фильтр по категориям
  public StatisticsQuery withCategories(String[] categories) {
    if (categories != null && categories.length > 0) {
      filters.add(transaction -> Arrays.asList(categories).contains(transaction.getCategory()));
    }
    return this;
  }

  // Добавляет фильтр по типу транзакции (доход/расход)
  public StatisticsQuery withType(boolean isIncome) {
    filters.add(transaction -> transaction.isIncome() == isIncome);
    return this;
  }

  // Добавляет фильтр по временному периоду
  public StatisticsQuery withPeriod(LocalDateTime dateFrom, LocalDateTime dateTo) {
    if (dateFrom != null) {
      filters.add(transaction -> !transaction.getCreatedAt().isBefore(dateFrom));
    }
    if (dateTo != null) {
      filters.add(transaction -> !transaction.getCreatedAt().isAfter(dateTo));
    }
    return this;
  }

  // Собирает все добавленные фильтры в единый предикат
  public Predicate<Transaction> build() {
    return filters.stream().reduce(transaction -> true, Predicate::and);
  }
}
