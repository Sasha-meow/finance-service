package oop.finance.service;

import oop.finance.exception.InvalidCredentialsException;
import oop.finance.exception.UserNotFoundException;
import oop.finance.model.User;
import oop.finance.repository.UserRepository;
import oop.finance.service.base.BaseService;

public class AuthService extends BaseService {
  private final UserRepository userRepository;
  private User currentUser;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public User getUserByLogin(String login) {
    if (!userRepository.existsByLogin(login)) {
      throw new UserNotFoundException("Юзер с таким именем не найден!");
    }
    return userRepository.findByLogin(login);
  }

  public void register(String login, String password) {
    validateNotNull(login, "Логин");
    validateNotNull(password, "Пароль");

    if (userRepository.existsByLogin(login)) {
      throw new InvalidCredentialsException("Такой пользователь уже зарегистрирован!");
    }

    userRepository.add(new User(login, password));
  }

  public void authenticate(String login, String password) {
    validateNotNull(login, "Логин");
    validateNotNull(password, "Пароль");

    User user = userRepository.findByLogin(login);

    if (user == null) {
      throw new UserNotFoundException("Юзер с таким именем не найден!");
    }

    if (!user.getPassword().equals(password)) {
      throw new InvalidCredentialsException("Пароль введен не верно!");
    }

    currentUser = user;
  }

  public void logout() {
    currentUser = null;
    userRepository.saveAll();
  }
}
