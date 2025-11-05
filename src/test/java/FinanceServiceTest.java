import static org.junit.jupiter.api.Assertions.*;

import oop.finance.exception.InvalidCredentialsException;
import oop.finance.model.Budget;
import oop.finance.model.User;
import oop.finance.service.FinanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FinanceServiceTest {
  private FinanceService financeService;
  private User user;
  private final String TEST_STRING = "test";
  private final String TEST_CATEGORY = "Еда";
  private final double TEST_AMOUNT = 1000.0;
  private final double TEST_EXPENSE = 300.0;
  private final double TEST_NEGATIVE = -300.0;

  @BeforeEach
  void setUp() {
    financeService = new FinanceService();
    user = new User(TEST_STRING, TEST_STRING);
  }

  @Test
  @DisplayName(
      "Метод добавления дохода с корректными данными должен работать без ошибок и обновлять баланс")
  void testAddIncome() {
    financeService.addIncome(user, TEST_STRING, TEST_AMOUNT);

    assertEquals(TEST_AMOUNT, user.getWallet().getBalance());
  }

  @Test
  @DisplayName("Метод добавления дохода с некорректными данными должен выбрасывать ошибку")
  void testAddIncomeWithInvalidAmount() {
    assertThrows(
        InvalidCredentialsException.class,
        () -> {
          financeService.addIncome(user, TEST_STRING, TEST_NEGATIVE);
        });
  }

  @Test
  @DisplayName("Метод добавления дохода с нулевой категорией должен выбрасывать ошибку")
  void testAddIncomeWithNullCategory() {
    assertThrows(
        InvalidCredentialsException.class,
        () -> {
          financeService.addIncome(user, null, TEST_AMOUNT);
        });
  }

  @Test
  @DisplayName(
      "Метод добавления дохода и расхода должен обновлять список транзакций и баланс без ошибок")
  void testAddExpense() {
    financeService.addIncome(user, TEST_STRING, TEST_AMOUNT);
    financeService.addExpense(user, TEST_CATEGORY, TEST_EXPENSE);

    assertEquals(TEST_AMOUNT - TEST_EXPENSE, user.getWallet().getBalance());
    assertEquals(2, user.getWallet().getTransactions().size());
  }

  @Test
  @DisplayName("Добавление бюджета с валидными данными должно отрабатывать без ошибок")
  void testSetBudget() {
    financeService.setBudget(user, TEST_CATEGORY, TEST_EXPENSE);

    Budget budget = user.getWallet().getBudget(TEST_CATEGORY);

    assertEquals(TEST_CATEGORY, budget.getCategory());
    assertEquals(TEST_EXPENSE, budget.getLimit());
  }

  @Test
  @DisplayName("Добавление нескольких доходов должно корректно обновлять бюджет")
  void testGetTotalIncome() {
    financeService.addIncome(user, TEST_STRING, TEST_AMOUNT);
    financeService.addIncome(user, TEST_CATEGORY, TEST_EXPENSE);

    double totalIncome = financeService.getTotalIncome(user);
    assertEquals(TEST_AMOUNT + TEST_EXPENSE, totalIncome);
  }

  @Test
  @DisplayName("Метод проверки баланса на уход в минус должен работать корректно")
  void testIsTotalExceeding() {
    financeService.addIncome(user, TEST_STRING, TEST_EXPENSE);
    financeService.addExpense(user, TEST_CATEGORY, TEST_AMOUNT);

    assertTrue(financeService.isTotalExceeding(user));
  }
}
