package oop.finance.repository;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oop.finance.model.User;
import oop.finance.repository.base.BaseRepository;
import oop.finance.utils.JsonUtils;

public class UserRepository implements BaseRepository<User> {
  private final JsonUtils jsonUtils = new JsonUtils();
  private final Map<String, User> users;
  private String filePath;
  private final Type usersMapType =
      new TypeToken<Map<String, User>>() {
        // комментарий для скипа формата данной строки
      }.getType();

  public UserRepository(String path) {
    this.filePath = path;
    this.users = load();
  }

  public User findByLogin(String login) {
    return users.get(login);
  }

  public boolean existsByLogin(String login) {
    return users.containsKey(login);
  }

  @Override
  public void add(User user) {
    users.put(user.getLogin(), user);
  }

  @Override
  public List<User> list() {
    return new ArrayList<>(users.values());
  }

  @Override
  public Map<String, User> load() {
    try {
      Map<String, User> loadedUsers = jsonUtils.readFromJSON(filePath, usersMapType);
      return loadedUsers != null ? loadedUsers : new HashMap<>();
    } catch (JsonSyntaxException error) {
      System.out.println("Ошибка загрузки пользователей: " + error.getMessage());
      System.out.println("Создаю новую базу пользователей...");
      return new HashMap<>();
    }
  }

  @Override
  public void saveAll() {
    jsonUtils.writeToJSON(filePath, users);
  }
}
