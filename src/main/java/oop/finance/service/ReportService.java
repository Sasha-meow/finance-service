package oop.finance.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import oop.finance.model.Report;
import oop.finance.model.User;
import oop.finance.utils.DateTimeUtils;

/** Сервис генерации CSV отчетов. Предоставляет методы для выгрузки в формат CSV */
public class ReportService {
  // Выгрузка отчета для юзера по заданному адресу
  public String generateReport(User user, String filePath) {
    try {
      Report report = new Report(user);
      String content = report.generateCsv();

      Files.write(Paths.get(filePath), content.getBytes());
      return filePath;
    } catch (IOException error) {
      throw new RuntimeException("Ошибка при сохранении отчета: " + error.getMessage());
    }
  }

  // Выгрузка отчета для юзера по дефолтному адресу
  public String generateReport(User user) {
    String fileName =
        String.format(
            "report_%s.csv",
            LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(DateTimeUtils.DATE_TIME_FORMAT_REPORT)));

    return generateReport(user, fileName);
  }
}
