import static org.junit.jupiter.api.Assertions.*;

import oop.finance.exception.InvalidCredentialsException;
import oop.finance.model.Budget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BudgetTest {
  private Budget budget;
  private final String TEST_CATEGORY = "Еда";
  private final double TEST_AMOUNT = 1000.0;
  private final double TEST_EXPENSE = 300.0;
  private final double TEST_NEGATIVE = -300.0;

  @BeforeEach
  void setUp() {
    budget = new Budget(TEST_CATEGORY, TEST_AMOUNT);
  }

  @Test
  @DisplayName("Конструктор с валидными пропсами должен создавать сущность без ошибок")
  void testBudgetCreationWithValidLimit() {
    assertEquals(TEST_CATEGORY, budget.getCategory());
    assertEquals(TEST_AMOUNT, budget.getLimit());
    assertEquals(0.0, budget.getSpent());
    assertFalse(budget.isExceeded());
  }

  @Test
  @DisplayName("Конструктор с отрицательным лимитом должен выбрасывать ошибку")
  void testBudgetCreationWithNegativeLimit() {
    assertThrows(
        InvalidCredentialsException.class,
        () -> {
          new Budget(TEST_CATEGORY, TEST_NEGATIVE);
        });
  }

  @Test
  @DisplayName("Добавление расхода должно отрабатывать без ошибок и менять бюджет соответственно")
  void testAddExpense() {
    budget.addExpense(TEST_EXPENSE);

    assertEquals(TEST_EXPENSE, budget.getSpent());
    assertEquals(TEST_AMOUNT - TEST_EXPENSE, budget.getRemaining());
    assertFalse(budget.isExceeded());
  }

  @Test
  @DisplayName("Функция превышения бюджета работает корректно")
  void testBudgetExceeded() {
    budget.addExpense(TEST_AMOUNT + TEST_EXPENSE);

    assertTrue(budget.isExceeded());
  }

  @Test
  @DisplayName("Функция изменения лимита с корректными данными работает без ошибок")
  void testSetValidLimit() {
    budget.setLimit(TEST_EXPENSE);

    assertEquals(TEST_EXPENSE, budget.getLimit());
  }

  @Test
  @DisplayName("Функция изменения лимита с некорректными данными выбрасывает ошибку")
  void testSetInvalidLimit() {
    assertThrows(
        InvalidCredentialsException.class,
        () -> {
          budget.setLimit(TEST_NEGATIVE);
        });
  }
}
