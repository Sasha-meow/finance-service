import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Properties;
import oop.finance.utils.ConfigReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConfigReaderTest {
  private ConfigReader configReader;

  @BeforeEach
  public void setUp() {
    configReader = new ConfigReader();
  }

  @Test
  @DisplayName("Метод считывания должен работать без ошибок")
  public void testLoad() {
    assertDoesNotThrow(() -> configReader.load());
  }

  @Test
  @DisplayName("Существующее свойство должно быть не null")
  public void testGetExistingProperty() throws IOException {
    configReader.load();
    assertNotNull(configReader.getProperties().getProperty("users.src"));
  }

  @Test
  @DisplayName("Несуществующее свойство должно быть null")
  public void testDoesntGetNotExistingProperty() {
    Properties props = configReader.getProperties();
    assertNull(props.getProperty("test"));
  }
}
