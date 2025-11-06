package oop.finance.model;

import java.util.Objects;
import oop.finance.model.base.BaseEntity;

/** Модель юзера */
public class User extends BaseEntity {
  private String login;
  private String password;
  private Wallet wallet;

  public User(String login, String password) {
    this.login = login;
    this.password = password;
    this.wallet = new Wallet(id);
  }

  // Геттеры
  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public Wallet getWallet() {
    return wallet;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(login, user.login);
  }
}
