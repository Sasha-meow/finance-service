package oop.finance.presentation;

import oop.finance.model.User;
import oop.finance.presentation.base.BaseUI;
import oop.finance.service.AuthService;
import oop.finance.service.ReportService;

/**
 * Класс-обработчик пользовательского интерфейса для работы с отчетами. Вынесен отдельно для
 * масштабирования: планируется добавить выгрузки в другие форматы
 */
public class ReportsHandler extends BaseUI {
  private final AuthService authService;
  private final ReportService reportService;

  public ReportsHandler(AuthService authService, ReportService reportService) {
    this.authService = authService;
    this.reportService = reportService;
  }

  // Обрабатывает выгрузку отчета в CSV
  public void handleExportReport() {
    try {
      String path =
          readStringWithEmptiness(
              "Введите путь для сохранения файла (или нажмите Enter для сохранения в текущей папке): ");
      String resultPath;

      User user = authService.getCurrentUser();
      if (path.isEmpty()) {
        resultPath = reportService.generateReport(user);
      } else {
        resultPath = reportService.generateReport(user, path + ".csv");
      }

      printSuccessNotification("Отчёт " + resultPath + " успешно выгружен!");
    } catch (Exception error) {
      printErrorNotification("Ошибка при выгрузке отчета: " + error.getMessage());
    }
  }
}
