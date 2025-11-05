import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import oop.finance.exception.InvalidCredentialsException;
import oop.finance.exception.UserNotFoundException;
import oop.finance.model.User;
import oop.finance.repository.UserRepository;
import oop.finance.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthServiceTest {
  private AuthService authService;
  private UserRepository userRepository;
  private final String TEST_STRING = "test";

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    authService = new AuthService(userRepository);
  }

  @Test
  @DisplayName("Метод регистрации должен работать без ошибок при корректных входных данных")
  void testSuccessfulRegistration() {
    when(userRepository.existsByLogin(TEST_STRING)).thenReturn(false);

    authService.register(TEST_STRING, TEST_STRING);

    verify(userRepository).add(any(User.class));
  }

  @Test
  @DisplayName(
      "Метод регистрации должен выбрасывать ошибку при регистрации юзера, который уже есть")
  void testRegistrationWithExistingUser() {
    when(userRepository.existsByLogin(TEST_STRING)).thenReturn(true);

    assertThrows(
        InvalidCredentialsException.class,
        () -> {
          authService.register(TEST_STRING, TEST_STRING);
        });
  }

  @Test
  @DisplayName("Метод регистрации должен выбрасывать ошибку при регистрации юзера без пароля")
  void testRegistrationWithNullLogin() {
    assertThrows(
        InvalidCredentialsException.class,
        () -> {
          authService.register(null, TEST_STRING);
        });
  }

  @Test
  @DisplayName(
      "Метод авторизации должен корректно авторизовывать существующего юзера при вводе корректных данных")
  void testSuccessfulAuthentication() {
    User mockUser = new User(TEST_STRING, TEST_STRING);
    when(userRepository.findByLogin(TEST_STRING)).thenReturn(mockUser);

    authService.authenticate(TEST_STRING, TEST_STRING);

    assertNotNull(authService.getCurrentUser());
    assertEquals(TEST_STRING, authService.getCurrentUser().getLogin());
  }

  @Test
  @DisplayName("Метод авторизации должен выбрасывать ошибку при вводе некорректного пароля")
  void testAuthenticationWithWrongPassword() {
    User mockUser = new User(TEST_STRING, TEST_STRING);
    when(userRepository.findByLogin(TEST_STRING)).thenReturn(mockUser);

    assertThrows(
        InvalidCredentialsException.class,
        () -> {
          authService.authenticate(TEST_STRING, "123");
        });
  }

  @Test
  @DisplayName("Метод авторизации должен выбрасывать ошибку при вводе некорректного логина")
  void testAuthenticationWithNonExistentUser() {
    when(userRepository.findByLogin(TEST_STRING)).thenReturn(null);

    assertThrows(
        UserNotFoundException.class,
        () -> {
          authService.authenticate(TEST_STRING, TEST_STRING);
        });
  }

  @Test
  @DisplayName("Метод логаута должен работать без ошибок")
  void testLogout() {
    User mockUser = new User(TEST_STRING, TEST_STRING);
    when(userRepository.findByLogin(TEST_STRING)).thenReturn(mockUser);

    authService.authenticate(TEST_STRING, TEST_STRING);
    assertNotNull(authService.getCurrentUser());

    authService.logout();
    assertNull(authService.getCurrentUser());
  }
}
