package oop.finance.presentation.base;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Базовый класс слоя презентации. Предоставляет основные методы чтения с консоли, вывод нотификаций
 */
public abstract class BaseUI {
  protected final String skullEmoji = "\uD83D\uDC80";
  protected final String partyEmoji = "\uD83C\uDF89";
  protected final String warningEmoji = "\u26A0\uFE0F";
  protected final Scanner scanner;

  protected BaseUI() {
    this.scanner = new Scanner(System.in);
  }

  // Чтение числа с плавающей запятой
  protected double readDouble(String str) {
    while (true) {
      System.out.print(str);
      try {
        return scanner.nextDouble();
      } catch (NumberFormatException | InputMismatchException e) {
        printErrorNotification("Некорректный формат числа!");
      }
    }
  }

  // Чтение строки
  protected String readString(String str) {
    System.out.print(str);
    return scanner.next().trim();
  }

  // Чтение строки с учетом Enter
  protected String readStringWithEmptiness(String str) {
    System.out.print(str);
    return scanner.nextLine().trim();
  }

  // Вывод сообщения об успехе зеленым цветом
  protected void printSuccessNotification(String str) {
    System.out.println(partyEmoji + "\u001B[32m" + str + "\u001B[0m");
  }

  // Вывод сообщения об ошибке красным цветом
  protected void printErrorNotification(String str) {
    System.out.println(skullEmoji + "\u001B[31m" + str + "\u001B[0m");
  }

  // Вывод сообщения желтым цветом
  protected void printInfoNotification(String str) {
    System.out.println(warningEmoji + "\u001B[33m" + str + "\u001B[0m");
  }

  // Справка
  protected void showHelp() {
    System.out.println("Справка:");
    System.out.println(
        "Программа для контроля бюджета. Для начала использования - необходимо зарегистрироваться.");
    System.out.println("После регистрации необходимо авторизоваться!");
    System.out.println(
        "При завершении работы/выходе пользователя сохраняет данные в файл data.json в корень проекта.");
    System.out.println(
        "При генерации отчета с дефолтным наименованием сохраняет его в корень проекта в файл report_дата.csv.");
    System.out.println(
        "Внимание! Рекомендуется следовать инструкциям заполнения данных. Все данные тщательно валидируются.");
  }
}
