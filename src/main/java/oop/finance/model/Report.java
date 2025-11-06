package oop.finance.model;

import java.time.format.DateTimeFormatter;
import oop.finance.model.base.BaseEntity;
import oop.finance.utils.DateTimeUtils;

/** Модель отчета. В будущем планируется добавить JSON формат */
public class Report extends BaseEntity {
  protected final User user;

  public Report(User user) {
    this.user = user;
  }

  // Генерация таблицы для CSV
  public String generateCsv() {
    StringBuilder csv = new StringBuilder();

    csv.append(
        String.format(
            "Отчет кошелька %s\n",
            createdAt.format(DateTimeFormatter.ofPattern(DateTimeUtils.DATE_TIME_FORMAT))));
    csv.append("Тип,Категория,Сумма,Дата\n");

    for (Transaction transaction : user.getWallet().getTransactions()) {
      String type = transaction.isIncome() ? "Доход" : "Расход";
      csv.append(
          String.format(
              "%s,%s,%.2f,%s\n",
              type,
              transaction.getCategory(),
              transaction.getAmount(),
              transaction
                  .getCreatedAt()
                  .format(DateTimeFormatter.ofPattern(DateTimeUtils.DATE_TIME_FORMAT))));
    }

    return csv.toString();
  }
}
