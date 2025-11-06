package oop.finance.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * Утилитарный класс для работы с JSON данными. Предоставляет методы для сериализации и
 * десериализации JSON
 */
public class JsonUtils {
  private final Gson gson;

  public JsonUtils() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
    this.gson = gsonBuilder.setPrettyPrinting().create();
  }

  // Десериализует json в переданный класс
  public <T> T readFromJSON(String filePath, Type type) throws JsonSyntaxException {
    try (FileReader reader = new FileReader(filePath)) {
      return gson.fromJson(reader, type);
    } catch (IOException | JsonSyntaxException error) {
      throw new JsonSyntaxException("Некорректные JSON данные!");
    }
  }

  // Cериализует объект в JSON
  public <T> void writeToJSON(String filePath, T obj) throws JsonSyntaxException {
    try (FileWriter writer = new FileWriter(filePath)) {
      gson.toJson(obj, writer);
    } catch (IOException | JsonSyntaxException error) {
      throw new JsonSyntaxException("Некорректные JSON данные!");
    }
  }
}
