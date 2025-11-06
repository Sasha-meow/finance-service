package oop.finance.service;

import oop.finance.exception.InvalidCredentialsException;
import oop.finance.exception.UserNotFoundException;
import oop.finance.model.User;
import oop.finance.repository.UserRepository;
import oop.finance.service.base.BaseService;

/** Сервис авторизации. Предоставляет методы для работы с юзером и его сессией */
public class AuthService extends BaseService {
  private final UserRepository userRepository;
  private User currentUser;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // Возвращает текущего аутентифицированного пользователя
  public User getCurrentUser() {
    return currentUser;
  }

  // Находит пользователя по логину
  public User getUserByLogin(String login) {
    if (!userRepository.existsByLogin(login)) {
      throw new UserNotFoundException("Юзер с таким именем не найден!");
    }
    return userRepository.findByLogin(login);
  }

  // Регистрирует нового пользователя в системе
  public void register(String login, String password) {
    validateNotNull(login, "Логин");
    validateNotNull(password, "Пароль");

    if (userRepository.existsByLogin(login)) {
      throw new InvalidCredentialsException("Такой пользователь уже зарегистрирован!");
    }

    userRepository.add(new User(login, password));
  }

  // Выполняет аутентификацию пользователя
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

  // Выполняет выход пользователя из системы
  public void logout() {
    currentUser = null;
    userRepository.saveAll();
  }
}
