package oop.finance.presentation.base;

import java.util.Scanner;

public abstract class BaseUI {
  protected final String skullEmoji = "\uD83D\uDC80";
  protected final String partyEmoji = "\uD83C\uDF89";
  protected final String warningEmoji = "\u26A0\uFE0F";
  protected final Scanner scanner;

  protected BaseUI() {
    this.scanner = new Scanner(System.in);
  }

  protected double readDouble(String str) {
    while (true) {
      System.out.print(str);
      try {
        return scanner.nextDouble();
      } catch (NumberFormatException e) {
        printErrorNotification("Некорректный формат числа!");
      }
    }
  }

  protected String readString(String str) {
    System.out.print(str);
    return scanner.next().trim();
  }

  protected String readStringWithEmptiness(String str) {
    System.out.print(str);
    scanner.nextLine();
    return scanner.nextLine().trim();
  }

  protected void printSuccessNotification(String str) {
    System.out.println(partyEmoji + "\u001B[32m" + str + "\u001B[0m");
  }

  protected void printErrorNotification(String str) {
    System.out.println(skullEmoji + "\u001B[31m" + str + "\u001B[0m");
  }

  protected void printInfoNotification(String str) {
    System.out.println(warningEmoji + "\u001B[33m" + str + "\u001B[0m");
  }
}
