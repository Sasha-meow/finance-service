package oop.finance.model.base;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/** Базовая модель сущности. Содержит дату создания и идентификатор */
public abstract class BaseEntity implements Serializable {
  protected String id;
  protected final LocalDateTime createdAt;

  protected BaseEntity() {
    this.id = UUID.randomUUID().toString();
    this.createdAt = LocalDateTime.now();
  }

  public String getId() {
    return id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof BaseEntity)) {
      return false;
    }

    BaseEntity entity = (BaseEntity) o;
    return id.equals(entity.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
