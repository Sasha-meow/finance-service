package oop.finance.model;

import java.time.format.DateTimeFormatter;
import oop.finance.constants.BaseConstants;
import oop.finance.model.base.BaseEntity;

public class Report extends BaseEntity {
  protected final User user;

  public Report(User user) {
    this.user = user;
  }

  public String generateCsv() {
    StringBuilder csv = new StringBuilder();

    csv.append(
        String.format(
            "Отчет кошелька %s\n",
            createdAt.format(DateTimeFormatter.ofPattern(BaseConstants.DATE_TIME_FORMAT))));
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
                  .format(DateTimeFormatter.ofPattern(BaseConstants.DATE_TIME_FORMAT))));
    }

    return csv.toString();
  }
}
