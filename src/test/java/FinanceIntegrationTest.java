import static org.junit.jupiter.api.Assertions.*;

import oop.finance.model.Budget;
import oop.finance.model.User;
import oop.finance.repository.UserRepository;
import oop.finance.service.AuthService;
import oop.finance.service.BudgetService;
import oop.finance.service.FinanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FinanceIntegrationTest {
  private AuthService authService;
  private FinanceService financeService;
  private BudgetService budgetService;
  private UserRepository userRepository;
  private final String USER1 = "user1";
  private final String USER2 = "user2";
  private final String SALARY = "salary";
  private final String FOOD = "food";
  private final String TAXI = "taxi";
  private final double AMOUNT_MAX = 3000.0;
  private final double AMOUNT_MEDIUM = 300.0;
  private final double AMOUNT_MIN = 30.0;

  @BeforeEach
  void setUp() {
    userRepository = new UserRepository("test_users.json");
    authService = new AuthService(userRepository);
    financeService = new FinanceService();
    budgetService = new BudgetService();
  }

  @Test
  @DisplayName("Полный пользовательский цикл")
  void testCompleteUserFinancialWorkflow() {
    // 1. Регистрация пользователя
    authService.register(USER1, USER1);

    // 2. Авторизация
    authService.authenticate(USER1, USER1);
    User user = authService.getCurrentUser();

    // 3. Добавление доходов
    financeService.addIncome(user, SALARY, AMOUNT_MAX);

    // 4. Установка бюджетов
    budgetService.setBudget(user, FOOD, AMOUNT_MEDIUM);
    budgetService.setBudget(user, TAXI, AMOUNT_MEDIUM);

    // 5. Добавление расходов
    financeService.addExpense(user, FOOD, AMOUNT_MIN);
    financeService.addExpense(user, TAXI, AMOUNT_MIN);
    financeService.addExpense(user, FOOD, AMOUNT_MIN);

    // 6. Проверка результатов
    assertEquals(AMOUNT_MAX - (3 * AMOUNT_MIN), user.getWallet().getBalance());
    assertEquals(AMOUNT_MAX, financeService.getTotalIncome(user));
    assertEquals(3 * AMOUNT_MIN, financeService.getTotalExpense(user));

    Budget foodBudget = user.getWallet().getBudget(FOOD);
    assertEquals(AMOUNT_MIN * 2, foodBudget.getSpent());
    assertEquals(AMOUNT_MEDIUM - AMOUNT_MIN * 2, foodBudget.getRemaining());
    assertFalse(foodBudget.isExceeded());
  }

  @Test
  @DisplayName("Кейс превышения бюджета")
  void testBudgetExceededScenario() {
    authService.register(USER1, USER1);
    authService.authenticate(USER1, USER1);
    User user = authService.getCurrentUser();

    financeService.addIncome(user, SALARY, AMOUNT_MAX);

    budgetService.setBudget(user, TAXI, AMOUNT_MIN);

    financeService.addExpense(user, TAXI, AMOUNT_MEDIUM);

    Budget shoppingBudget = user.getWallet().getBudget(TAXI);
    assertTrue(shoppingBudget.isExceeded());
    assertEquals(AMOUNT_MIN - AMOUNT_MEDIUM, shoppingBudget.getRemaining());
  }

  @Test
  @DisplayName("Кейс перевода между юзерами")
  void testTransferBetweenUsers() {
    // Создаем двух пользователей
    authService.register(USER1, USER1);
    authService.register(USER2, USER2);

    authService.authenticate(USER1, USER1);
    User user1 = authService.getCurrentUser();
    financeService.addIncome(user1, SALARY, AMOUNT_MAX);

    authService.authenticate(USER2, USER2);
    User user2 = authService.getCurrentUser();

    // Возвращаемся к первому пользователю для перевода
    authService.authenticate(USER1, USER1);

    financeService.makeTransfer(user1, user2, AMOUNT_MEDIUM);

    assertEquals(AMOUNT_MAX - AMOUNT_MEDIUM, user1.getWallet().getBalance());

    // Проверяем второго пользователя
    authService.authenticate(USER2, USER2);
    assertEquals(AMOUNT_MEDIUM, user2.getWallet().getBalance());
  }
}
