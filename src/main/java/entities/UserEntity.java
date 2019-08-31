package entities;

import models.User;

public final class UserEntity extends User {
  private String id;
  private Long createdAt;
  private Long updatedAt;

  public UserEntity(String id, String email, String password, Long createdAt, Long updatedAt) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public String getId() {
    return id;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }
}
