package oop.finance.repository.base;

import java.util.List;
import java.util.Map;

/** Базовый интерфейс репозитория. Описывает методы добавления, получения, загрузки и сохранения */
public interface BaseRepository<T> {
  void add(T data);

  Map<String, T> load();

  List<T> list();

  void saveAll();
}
