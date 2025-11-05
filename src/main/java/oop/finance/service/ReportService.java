package oop.finance.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import oop.finance.model.Report;
import oop.finance.model.User;

public class ReportService {
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

  public String generateReport(User user) {
    String fileName =
        String.format(
            "report_%s.csv",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")));

    return generateReport(user, fileName);
  }
}
