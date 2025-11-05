import static org.junit.jupiter.api.Assertions.*;

import oop.finance.model.Budget;
import oop.finance.model.Transaction;
import oop.finance.model.User;
import oop.finance.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WalletTest {
  private Wallet wallet;
  private User user;
  private final String TEST_CATEGORY = "Еда";
  private final double TEST_AMOUNT = 1000.0;
  private final double TEST_MIN_AMOUNT = 300.0;
  private final String TEST_STRING = "test";

  @BeforeEach
  void setUp() {
    user = new User(TEST_STRING, TEST_STRING);
    wallet = user.getWallet();
  }

  @Test
  @DisplayName("Метод добавления дохода с корректными данными должен работать без ошибок")
  void testAddIncomeTransaction() {
    Transaction income = new Transaction(TEST_AMOUNT, TEST_STRING, true);
    wallet.addTransaction(income);

    assertEquals(TEST_AMOUNT, wallet.getBalance());
  }

  @Test
  @DisplayName("Метод добавления расхода с корректными данными должен работать без ошибок")
  void testAddExpenseTransaction() {
    Transaction income = new Transaction(TEST_AMOUNT, TEST_STRING, true);
    wallet.addTransaction(income);
    Transaction expense = new Transaction(TEST_MIN_AMOUNT, TEST_CATEGORY, false);
    wallet.addTransaction(expense);

    assertEquals(TEST_AMOUNT - TEST_MIN_AMOUNT, wallet.getBalance());
    assertEquals(2, wallet.getTransactions().size());
  }

  @Test
  @DisplayName("Метод добавления бюджета с корректными данными должен работать без ошибок")
  void testSetBudget() {
    Budget budget = new Budget(TEST_CATEGORY, TEST_MIN_AMOUNT);
    wallet.setBudget(budget);

    Budget retrievedBudget = wallet.getBudget(TEST_CATEGORY);
    assertNotNull(retrievedBudget);
    assertEquals(TEST_CATEGORY, retrievedBudget.getCategory());
    assertEquals(TEST_MIN_AMOUNT, retrievedBudget.getLimit());
  }
}
